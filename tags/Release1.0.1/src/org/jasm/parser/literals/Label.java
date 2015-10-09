package org.jasm.parser.literals;

public class Label extends AbstractLiteral {

	public Label(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getLabel() {
		return getContent();
	}

}
