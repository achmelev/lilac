package org.jasm.parser.literals;


public class IntegerLiteral extends AbstractLiteral {
	
	public IntegerLiteral(int line, int charPosition, String content) {
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
	
	
	
	public int getValue() throws NumberFormatException {
		String content = getContent();
		char sign='+';
		int result = -1;
		if (content.charAt(0)=='-' || content.charAt(0)=='+') {
			sign=content.charAt(0);
			content = content.substring(1, content.length());
		}
		
		if (content.startsWith("0x") || content.startsWith("0X")) { //hexadecimal
			result = Integer.parseInt(sign+content.substring(2, content.length()),16);
		} else if (content.startsWith("0b") || content.startsWith("0B")) { //hexadecimal
			result = Integer.parseInt(sign+content.substring(2, content.length()),2);
		} else if (content.startsWith("0")) {//oktal
			result = Integer.parseInt(sign+content,8);
		} else {
			result = Integer.parseInt(sign+content,10);
		}
		
		if ((sign == '+' && result<0) ||  (sign == '-' && result>0)) {
			throw new NumberFormatException("input string "+getContent());
		}
		
		return result;
	}

}
