package org.jasm.parser.literals;

import org.jasm.item.instructions.macros.IMacroArgument;


public class LongLiteral extends AbstractLiteral implements IMacroArgument {
	
	public LongLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public boolean isValid() {
		try {
			getValue();
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	
	
	public long getValue() throws NumberFormatException {
		String content = getContent();
		char sign='+';
		long result = -1;
		if (content.charAt(0)=='-' || content.charAt(0)=='+') {
			sign=content.charAt(0);
			content = content.substring(1, content.length());
		}
		
		if (content.startsWith("0x") || content.startsWith("0X")) { //hexadecimal
			result = Long.parseLong(sign+content.substring(2, content.length()),16);
		} else if (content.startsWith("0b") || content.startsWith("0B")) { //hexadecimal
			result = Long.parseLong(sign+content.substring(2, content.length()),2);
		} else if (content.startsWith("0")) {//oktal
			result = Long.parseLong(sign+content,8);
		} else {
			result = Long.parseLong(sign+content,10);
		}
		
		if ((sign == '+' && result<0) ||  (sign == '-' && result>0)) {
			throw new NumberFormatException("input string "+getContent());
		}
		
		return result;
	}

}
