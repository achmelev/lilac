package org.jasm.parser.literals;

import org.jasm.parser.SourceLocation;

public abstract class AbstractLiteral {
	private int line = -1;
	private int charPosition = -1;
	private String content;
	
	private SourceLocation sourceLocation;
	
	public AbstractLiteral(int line, int charPosition, String content) {
		this.line = line;
		this.charPosition = charPosition;
		this.content = content;
		this.sourceLocation = new SourceLocation(line, charPosition);
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

	@Override
	public String toString() {
		return getContent();
	}

	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}
	
	
	
	
}
