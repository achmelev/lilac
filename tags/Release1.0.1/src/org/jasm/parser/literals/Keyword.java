package org.jasm.parser.literals;

public class Keyword extends AbstractLiteral {

	public Keyword(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getKeyword() {
		return getContent();
	}

}
