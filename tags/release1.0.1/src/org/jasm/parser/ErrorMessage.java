package org.jasm.parser;

public class ErrorMessage {
	
	private int lineNumber = -1;
	private int charPosition = -1;
	private String message = null;
	
	public ErrorMessage(int lineNumber, int charPosition, String message) {
		this.lineNumber = lineNumber;
		this.charPosition = charPosition;
		this.message = message;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getCharPosition() {
		return charPosition;
	}

	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return "line " + lineNumber + ":" + charPosition + " " + message;
	}

}
