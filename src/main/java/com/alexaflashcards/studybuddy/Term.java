package com.alexaflashcards.studybuddy;

public class Term {

	private String term;
	private String lang;
	private boolean star;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public boolean isStar() {
		return star;
	}

	public void setStar(boolean star) {
		this.star = star;
	}

	public Term(String term1, String lang1) {
		this.term = term1;
		this.lang = lang1;
	}
}
