package org.jasm.parser.literals;

public abstract class AbstractLiteral {
	private int line = -1;
	private int charPosition = -1;
	private String content;
	
	public AbstractLiteral(int line, int charPosition, String content) {
		this.line = line;
		this.charPosition = charPosition;
		this.content = content;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getCharPosition() {
		return charPosition;
	}

	public String getContent() {
		return content;
	}
	
	
}
