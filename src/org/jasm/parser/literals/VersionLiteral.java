package org.jasm.parser.literals;

public class VersionLiteral extends AbstractLiteral {

	public VersionLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getValue() {
		return getContent();
	}

}
