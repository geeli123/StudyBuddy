package com.alexaflashcards.studybuddy;

public class Attributes {

	private String SESSION;
	private int QUESTION;
	private boolean CONTINUE;
	private String TERM;
	private String DEFINITION;
	private boolean CREATE;

	public int getCORRECT() {
		return CORRECT;
	}

	public void setCORRECT(int cORRECT) {
		CORRECT = cORRECT;
	}

	public int getWRONG() {
		return WRONG;
	}

	public void setWRONG(int wRONG) {
		WRONG = wRONG;
	}

	private int CORRECT;
	private int WRONG;

	public String getSESSION() {
		return SESSION;
	}

	public void setSESSION(String sESSION) {
		SESSION = sESSION;
	}

	public int getQUESTION() {
		return QUESTION;
	}

	public void setQUESTION(int qUESTION) {
		QUESTION = qUESTION;
	}

	public boolean isCONTINUE() {
		return CONTINUE;
	}

	public void setCONTINUE(boolean cONTINUE) {
		CONTINUE = cONTINUE;
	}

	public String getTERM() {
		return TERM;
	}

	public void setTERM(String tERM) {
		TERM = tERM;
	}

	public String getDEFINITION() {
		return DEFINITION;
	}

	public void setDEFINITION(String dEFINITION) {
		DEFINITION = dEFINITION;
	}

	public boolean isCREATE() {
		return CREATE;
	}

	public void setCREATE(boolean cREATE) {
		CREATE = cREATE;
	}

	public Attributes(String sESSION, int qUESTION, boolean cONTINUE, String tERM, String dEFINITION, boolean cREATE,
			int cORRECT, int wRONG) {
		super();
		SESSION = sESSION;
		QUESTION = qUESTION;
		CONTINUE = cONTINUE;
		TERM = tERM;
		DEFINITION = dEFINITION;
		CREATE = cREATE;
		CORRECT = cORRECT;
		WRONG = wRONG;
	}

}
