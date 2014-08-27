package org.jasm.parser.literals;

public class StringLiteral extends AbstractLiteral {

	public StringLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getStringValue() {
		//TODO - StringLiteral->String
		return getContent();
	}

}
