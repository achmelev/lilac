package org.jasm.parser;

public class SymbolReference {
	
	private int line = -1;
	private int charPosition = -1;
	private String name;
	
	public SymbolReference(int line, int charPosition, String name) {
		this.line = line;
		this.charPosition = charPosition;
		this.name = name;
	}

	public int getLine() {
		return line;
	}

	public int getCharPosition() {
		return charPosition;
	}

	public String getName() {
		return name;
	}
	
	

}
