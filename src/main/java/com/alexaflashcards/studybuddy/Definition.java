package com.alexaflashcards.studybuddy;

public class Definition {
	private String definition;
	private String lang;

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Definition(String definition1, String lang1) {
		this.definition = definition1;
		this.lang = lang1;
	}
}
