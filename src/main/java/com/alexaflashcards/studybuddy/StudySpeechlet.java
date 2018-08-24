package com.alexaflashcards.studybuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;

public class StudySpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(StudySpeechlet.class);
	private static final String SLOT_SESSION = "Session";
	private static final String SLOT_TERM = "Term";
	private static final String SLOT_DEFINITION = "Definition";
	private static final String SLOT_DEFINITION_ANSWER = "Definition";
	// private static final String SESSION_CREATE_OR_ACCESS =
	// "create_or_access";

	private static Attributes atr = new Attributes(null, 0, true, null, null, true, 0, 0);

	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any initialization logic goes here
	}

	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		String repromptText = "Now, what can I help you with?";
		String speechOutput = "Welcome to Study Buddy. I can help you study, just tell me to, "
				+ "create a new study session... " + repromptText;
		// If the user either does not reply to the welcome message or says
		// something that is not understood, they will be prompted again with
		// this text.

		// Here we are prompting the user for input
		return this.newAskResponse(speechOutput, repromptText);
	}

	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if ("setSessionIntent".equals(intentName)) {
			return createSession(intent, session);
			// } else if ("DialogueFlashcardIntent".equals(intentName)) {
			// // Determine if this turn is for city, for date, or an error.
			// // We could be passed slots with values, no slots, slots with no
			// // value.
			// Slot termSlot = intent.getSlot(SLOT_TERM);
			// Slot definitionSlot = intent.getSlot(SLOT_DEFINITION);
			// Slot sessionSlot = intent.getSlot(SLOT_SESSION);
			// if (termSlot != null && termSlot.getValue() != null) { // Alexa
			// // hears a
			// // term
			// return handleTermDialogueRequest(intent, session);
			// } else if (definitionSlot != null && definitionSlot.getValue() !=
			// null) { // Alexa
			// // hears
			// // a
			// // definition
			// return handleDefinitionDialogueRequest(intent, session);
			// } else if (sessionSlot != null && sessionSlot.getValue() != null)
			// { // Alexa
			// // hears
			// // a
			// // set
			// // name
			// return handleSessionDialogueRequest(intent, session);
			// } else { // Alexa isn't sure what it hears
			// // TODO: Implement default case
			// return getHelp();
			// }
		} else if ("setCardIntent".equals(intentName)) { // Alex hears a command
															// to create a card
			return setCard(intent, session);
		} else if ("reviewIntent".equals(intentName)) { // Alex hears a command
														// to create a card
			return review(intent, session);
		} else if ("getStatsIntent".equals(intentName)) { // Alex hears a
															// command to create
															// a card
			return getStats();
		} else if ("removeSessionIntent".equals(intentName)) { // Alexa hears a
			// command to
			// remove a set
			return removeSession(intent);
		} else if ("allSessionsIntent".equals(intentName)) { // Alexa hears a
			// command to
			// list out all
			// current
			// sessions
			return allSession(intent);
		} else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelp();
		} else if ("answerIntent".equals(intentName)) {
			return checkAnswer(intent, session);
		} else if ("AMAZON.YesIntent".equals(intentName)) {
			atr.setCONTINUE(true);
			if (!atr.isCREATE()) {
				return promptQuestion((String) atr.getSESSION(), session);
			} else {
				outputSpeech.setText("okay! ");
				return SpeechletResponse.newTellResponse(outputSpeech);
			}
		} else if ("AMAZON.NoIntent".equals(intentName)) {
			atr.setCONTINUE(false);
			outputSpeech.setText("okay! ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			outputSpeech.setText("Goodbye");

			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			return getHelp();
		}
	}

	private SpeechletResponse getStats() {
		int wins = atr.getCORRECT();
		int losses = atr.getWRONG();
		String wStr = Integer.toString(wins);
		String lStr = Integer.toString(losses);
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		String msg;
		if ((wins / losses) >= 1) {
			msg = "Alright!";
		} else if (wins == 0 && losses == 0) {
			msg = "";
		} else {
			msg = "Come on now!";
		}
		outputSpeech.setText("You have gotten " + wStr + " questions and missed " + lStr + " questions. " + msg);
		return SpeechletResponse.newTellResponse(outputSpeech);
	}

	private SpeechletResponse checkAnswer(Intent intent, final Session session) {
		// TODO Auto-generated method stub
		Slot answerSlot = intent.getSlot(SLOT_DEFINITION_ANSWER);
		String answerString = answerSlot.getValue();
		if (answerString.length() == 0) {
			String repromptSpeech = "What's your answer? ";
			String speechOutput = "Let's hear another answer. " + ". " + repromptSpeech;
			return newAskResponse(speechOutput, repromptSpeech);
		} else {
			if (answerString.toLowerCase().equals(atr.getDEFINITION())) {
				int c = atr.getCORRECT();
				c++;
				atr.setCORRECT(c);
				String repromptSpeech = "Would you like to continue? ";
				String speechOutput = "Correct! " + repromptSpeech;
				return newAskResponse(speechOutput, repromptSpeech);
			} else {
				int r = atr.getWRONG();
				r++;
				atr.setWRONG(r);
				String repromptSpeech = "Would you like to continue? ";
				String speechOutput = "Incorrect! The correct response is " + atr.getDEFINITION() + ". "
						+ repromptSpeech;
				return newAskResponse(speechOutput, repromptSpeech);
			}
		}
	}

	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	private SpeechletResponse allSession(Intent intent) {
		String sessionNames = Sessions.getAllSessionsText();
		if (sessionNames.length() == 0) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("There are currently no sessions. You're more than welcome to add one! ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("There are currently sessions for " + sessionNames + ". ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		}
	}

	private SpeechletResponse createSession(Intent intent, final Session session) {
		atr.setCREATE(true);
		// TODO Auto-generated method stub
		Slot sessionSlot = intent.getSlot(SLOT_SESSION);
		String sessionName = sessionSlot.getValue();
		if (Sessions.getSession(sessionName) == null) {
			HashMap<Term, Definition> flashcards = new HashMap<Term, Definition>();
			Sessions.setSession(sessionName, flashcards);
			atr.setSESSION(sessionName);
			atr.setQUESTION(0);
			atr.setCONTINUE(true);
			String repromptSpeech = "What's your term?";
			String speechOutput = "I've created the set " + sessionName + ". " + repromptSpeech;
			return newAskResponse(speechOutput, repromptSpeech);
		} else {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("The session " + sessionName + " already exists. ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		}
	}

	private SpeechletResponse review(Intent intent, final Session session) {
		atr.setCREATE(false);
		// TODO Auto-generated method stub
		Slot sessionSlot = intent.getSlot(SLOT_SESSION);
		String sessionName = sessionSlot.getValue();
		if (!sessionName.equals(atr.getSESSION())) {
			atr.setQUESTION(0);
		}
		HashMap<Term, Definition> sessionMap = Sessions.getSession(sessionName);
		if (sessionMap == null) {
			String repromptSpeech = "What else can I help with?";
			String speechOutput = "I'm sorry, the session " + sessionName + " doesn't seem to exist. " + repromptSpeech;
			repromptSpeech = "What else can I help with?";
			return newAskResponse(speechOutput, repromptSpeech);
		} else if (sessionMap != null && sessionMap.size() == 0) {
			String repromptSpeech = "What else can I help with?";
			String speechOutput = "I'm sorry, the session " + sessionName + " doesn't seem to have terms. "
					+ repromptSpeech;
			repromptSpeech = "What else can I help with?";
			return newAskResponse(speechOutput, repromptSpeech);
		} else if (sessionMap != null && sessionMap.size() != 0) {
			atr.setSESSION(sessionName);
			return promptQuestion(sessionName, session);
		} else {
			return getHelp();
		}
	}

	private SpeechletResponse promptQuestion(String sessionName, final Session session) {
		HashMap<Term, Definition> sessionMap = Sessions.getSession(atr.getSESSION());
		int question_num = atr.getQUESTION();
		int temp = question_num;
		question_num++;
		atr.setQUESTION(question_num);
		Set<Term> tempTransfer = sessionMap.keySet();
		ArrayList<Term> termList = new ArrayList<Term>();
		for (Term tempTerm : tempTransfer) {
			termList.add(tempTerm);
		}
		ArrayList<Definition> definitionList = new ArrayList<Definition>();
		for (int i = 0; i < termList.size(); i++) {
			definitionList.add(sessionMap.get(termList.get(i)));
		}
		if (temp > termList.size()) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("You've studied all the terms! Let's study something else.");
			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			Term curTerm = termList.get(temp);
			Definition curDef = definitionList.get(temp);
			String dispTerm = curTerm.getTerm();
			String dispDef = curDef.getDefinition();
			atr.setDEFINITION(dispDef);
			String repromptSpeech = "What's the definition?";
			String speechOutput = "What's the definition of " + dispTerm + "? ";
			return newAskResponse(speechOutput, repromptSpeech);
		}
	}

	private SpeechletResponse setCard(Intent intent, Session session) {
		// TODO Auto-generated method stub
		if (atr.isCONTINUE() && atr.getSESSION() != null) {
			Slot termSlot = intent.getSlot(SLOT_TERM);
			Slot defSlot = intent.getSlot(SLOT_DEFINITION);
			String termString = termSlot.getValue();
			String defString = defSlot.getValue();
			Definition curDefinition = new Definition(defString, "english");
			Term curTerm = new Term(termString, "english");
			String sessionName = atr.getSESSION();
			HashMap<Term, Definition> themap = Sessions.getSession(sessionName);
			themap.put(curTerm, curDefinition);
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Card added! ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		} else if (atr.getSESSION() == null) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("You must first define a study session. ");
			return SpeechletResponse.newTellResponse(outputSpeech);
		} else {
			return getHelp();
		}
	}

	// private SpeechletResponse handleDefinitionDialogueRequest(Intent intent,
	// final Session session) {
	// // TODO Auto-generated method stub
	// if (session.getAttribute(session_name) != null
	// && ((String) session.getAttribute(session_name)).equals("true")
	// && session.getAttribute(session_name) != null &&
	// session.getAttribute(session_name) != null) {
	// Slot defSlot = intent.getSlot(SLOT_DEFINITION);
	// String def = defSlot.getValue();
	// Definition curDefinition = new Definition(def, "english");
	// String sessionName = (String) session.getAttribute(session_name);
	// Term curTerm = (Term) session.getAttribute(session_name);
	// Map<Term, Definition> flashcards = Sessions.getSession(sessionName);
	// flashcards.put(curTerm, curDefinition);
	// session.setAttribute(session_name, null);
	// // Let user know that a definition was successfully added
	// PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	// outputSpeech.setText("Definition added! ");
	// return SpeechletResponse.newTellResponse(outputSpeech);
	// } else if (session.getAttribute(session_name) == null) {
	// PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	// outputSpeech.setText("You must first define a term. ");
	// return SpeechletResponse.newTellResponse(outputSpeech);
	// } else if (session.getAttribute(session_name) == null) {
	// PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	// outputSpeech.setText("You must first define a study session. ");
	// return SpeechletResponse.newTellResponse(outputSpeech);
	// } else {
	// // There was no current session
	// return getHelp();
	// }
	// }

	// private SpeechletResponse handleTermDialogueRequest(Intent intent, final
	// Session session) {
	// // TODO Auto-generated method stub
	// if (session.getAttribute(session_name) != null
	// && ((String) session.getAttribute(session_name)).equals("true")
	// && session.getAttribute(session_name) != null) {
	// Slot termSlot = intent.getSlot(SLOT_TERM);
	// String term = termSlot.getValue();
	// Term curTerm = new Term(term, "english", false);
	// session.setAttribute(session_name, true);
	//
	// PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	// outputSpeech.setText("Term added! Tell me the definition. ");
	// return SpeechletResponse.newTellResponse(outputSpeech);
	// // Reset the attribute session_name temp variable to null
	//
	// } else if (session.getAttribute(session_name) == null) {
	// PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	// outputSpeech.setText("You must first define a study session. ");
	// return SpeechletResponse.newTellResponse(outputSpeech);
	// } else {
	// // There was no current sesssion
	// return getHelp();
	// }
	// }

	// private SpeechletResponse studySession(Intent intent, final Session
	// session) {
	// Slot sessionSlot = intent.getSlot(SLOT_SESSION);
	// if (sessionSlot != null && sessionSlot.getValue() != null) {
	// String sessionName = sessionSlot.getValue();
	//
	//
	//
	// Map<Term, Definition> flashcards = Sessions.getSession(sessionName);
	//
	// if (flashcards !=null){
	// for (Map.Entry<Term, Definition> flashcard: flashcards.entrySet()){
	// String speechOutput =
	// "What is the definition of " + flashcard.getKey().getTerm();
	// String repromptSpeech = "I didn't quite catch that. What's the definition
	// of "+flashcard.getKey().getTerm()+"?";
	// return newAskResponse(speechOutput, repromptSpeech);
	// }
	// }else{
	// // We don't have a flashcard set called sessionName, so keep the session
	// open and ask the user for another
	// // item.
	// String speechOutput =
	// "I'm sorry, the session " + sessionName
	// + " doesn't seem to exist"
	// + ". What else can I help with?";
	// String repromptSpeech = "What else can I help with?";
	// return newAskResponse(speechOutput, repromptSpeech);
	// }
	// }
	// else{
	// return getHelp();
	// }
	// }

	private SpeechletResponse removeSession(Intent intent) {
		Slot sessionSlot = intent.getSlot(SLOT_SESSION);
		if (sessionSlot != null && sessionSlot.getValue() != null) {
			String sessionName = sessionSlot.getValue();

			// Get the flashcards (map) for the session
			Map<Term, Definition> flashcards = Sessions.getSession(sessionName);

			if (flashcards != null) {
				// If the session exists, remove it from the map
				Sessions.deleteSession(sessionName);

				// Create message to let user know the session has been removed
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setText("The session " + sessionName + " has been removed");

				return SpeechletResponse.newTellResponse(outputSpeech);
			} else {
				// We don't have a flashcard set called sessionName, so keep the
				// session open and ask the user for another
				// item.
				String speechOutput = "I'm sorry, the session " + sessionName + " doesn't seem to exist"
						+ ". What else can I help with?";
				String repromptSpeech = "What else can I help with?";
				return newAskResponse(speechOutput, repromptSpeech);
			}
		} else {
			// There was no item in the intent so return the help prompt.
			return getHelp();
		}
	}

	private SpeechletResponse getHelp() {
		String speechOutput = "You can study with me, tell me to do things like " + "Study Engineering Flashcards "
				+ "Delete Biology Flashcards " + "Create new flashcards called Math "
				+ "Now, what can I help you with?";
		String repromptText = "You can say things like, make new flashcards called Computer Science, "
				+ "or you can say exit... Now, what can I help you with?";
		return newAskResponse(speechOutput, repromptText);
	}

	/**
	 * Wrapper for creating the Ask response. The OutputSpeech and
	 * {@link Reprompt} objects are created from the input strings.
	 *
	 * @param stringOutput
	 *            the output to be spoken
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is
	 *            misunderstood.
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput, String repromptText) {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(stringOutput);

		PlainTextOutputSpeech repromptOutputSpeech = new PlainTextOutputSpeech();
		repromptOutputSpeech.setText(repromptText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);

		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}
}
