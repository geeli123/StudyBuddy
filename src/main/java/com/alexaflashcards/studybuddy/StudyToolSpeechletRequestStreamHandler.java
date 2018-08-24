package com.alexaflashcards.studybuddy;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * This class is created by the Lambda environment when a request comes in. All
 * calls will be dispatched to the Speechlet passed into the super constructor.
 */
public final class StudyToolSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
	private static final Set<String> supportedApplicationIds;

	static {
		/*
		 * This Id can be found on https://developer.amazon.com/edw/home.html#/
		 * "Edit" the relevant Alexa Skill and put the relevant Application Ids
		 * in this Set.
		 */
		supportedApplicationIds = new HashSet<String>();
		// supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
	}

	public StudyToolSpeechletRequestStreamHandler() {
		super(new StudySpeechlet(), supportedApplicationIds);
	}
}
