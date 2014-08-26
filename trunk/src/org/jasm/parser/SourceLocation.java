package org.jasm.parser;

public class SourceLocation {
	
	private int line = -1;
	private int charPosition = -1;
	
	public SourceLocation(int line, int charPosition) {
		this.line = line;
		this.charPosition = charPosition;
	}

	public int getLine() {
		return line;
	}

	public int getCharPosition() {
		return charPosition;
	}

	
	

}
