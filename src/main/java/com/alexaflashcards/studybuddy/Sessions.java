package com.alexaflashcards.studybuddy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class Sessions {

	private static Map<String, HashMap<Term, Definition>> allSessions = new HashMap<String, HashMap<Term, Definition>>();

	private Sessions() {
	}

	static {
		HashMap<Term, Definition> testMap = new HashMap<Term, Definition>();
		testMap.put(new Term("hi", "english"), new Definition("hello", "english"));
		testMap.put(new Term("bye", "english"), new Definition("goodbye", "english"));
		allSessions.put("test", testMap);
	}

	public static String getAllSessionsText() {
		StringBuilder sessionList = new StringBuilder();
		for (Map.Entry<String, HashMap<Term, Definition>> session : allSessions.entrySet()) {
			sessionList.append(session.getKey());
			sessionList.append(", ");
		}
		return sessionList.toString();
	}

	public static HashMap<Term, Definition> getSession(String ses) { // study
																		// sessions
		if (allSessions.containsKey(ses))
			return allSessions.get(ses);
		else return null;
	}

	public static void setSession(String name, HashMap<Term, Definition> map) { // create
																				// session
		if (allSessions.containsKey(name)) {
			allSessions.replace(name, map);
		} else {
			allSessions.put(name, map);
		}
	}

	public static void deleteSession(String name) { // remove session
		allSessions.remove(name);
	}

	public String toString() {
		String sessionList = "";
		Iterator it = allSessions.entrySet().iterator();
		StringBuilder combined = new StringBuilder(sessionList);
		while (it.hasNext()) {
			Map.Entry<String, HashMap<Term, Definition>> pair = (Map.Entry<String, HashMap<Term, Definition>>) it
					.next();
			combined.append("," + pair.getKey());
		}
		sessionList = combined.toString();
		return sessionList;
	}

}
