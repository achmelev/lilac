package org.jasm.parser.literals;

import org.apache.commons.lang3.StringEscapeUtils;

public class StringLiteral extends AbstractLiteral {

	public StringLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getStringValue() {
		return StringEscapeUtils.unescapeJava(getContent());
	}

}
