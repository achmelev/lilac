package org.jasm.parser.literals;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jasm.item.instructions.macros.IMacroArgument;

public class StringLiteral extends AbstractLiteral implements IMacroArgument {

	public StringLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getStringValue() {
		return StringEscapeUtils.unescapeJava(getContent().substring(1,getContent().length()-1));
	}

}
