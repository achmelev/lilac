package org.jasm.parser.literals;

public class FloatLiteral extends AbstractLiteral {

	public FloatLiteral(int line, int charPosition, String content) {
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
	
	public float getValue() {
		String content = getContent();
		char sign='+';
		float result = -1;
		if (content.charAt(0)=='-' || content.charAt(0)=='+') {
			sign=content.charAt(0);
			content = content.substring(1, content.length());
		}
		
		if (content.equals("NaN")) {
			result = Float.NaN;
		} else if (content.equals("Infinity")) {
			if (sign == '-') {
				result = Float.NEGATIVE_INFINITY;
			} else {
				result = Float.POSITIVE_INFINITY;
			}
		} else if (!(content.indexOf('p')>0 || content.indexOf('P')>0)){
			result = Float.parseFloat(sign+content);
		} else {
			result = parseFromPositiveExactBinaryLiteral(content);
			if (sign == '-') {
				result = -result;
			}
		}
		
		return result;
	}
	
	public static String createExactHexLiteral(float value) {
		
		if (Float.isNaN(value) || value == Float.NEGATIVE_INFINITY || value == Float.POSITIVE_INFINITY) {
			throw new NumberFormatException(""+value);
		}
		
		int bits = Float.floatToIntBits(value);
		
		int signBit = (bits>>31)&0x1;
		int exponentBits = (bits>>23)&0xff;
		int mantissBits = bits & 0x7fffff;
		
		StringBuffer buf = new StringBuffer();
		if (signBit == 1) {
			buf.append("-");
		}
		if (exponentBits == 0) {
			buf.append("0.");
		} else {
			buf.append("1.");
		}
		
		String[] mantisdigits = new String[6];
		
		mantisdigits[0] = Integer.toHexString((mantissBits>>19)&0xF);
		mantisdigits[1] = Integer.toHexString((mantissBits>>15)&0xF);
		mantisdigits[2] = Integer.toHexString((mantissBits>>11)&0xF);
		mantisdigits[3] = Integer.toHexString((mantissBits>>7)&0xF);
		mantisdigits[4] = Integer.toHexString((mantissBits>>3)&0xF);
		mantisdigits[5] = Integer.toHexString((mantissBits)&0xF);
		
		int lastNonZeroIndex = 5;
		while (lastNonZeroIndex>=0 && mantisdigits[lastNonZeroIndex].equals("0")) {
			lastNonZeroIndex--;
		}
		if (lastNonZeroIndex <0) {
			buf.append("0");
		} else {
			for (int i=0;i<=lastNonZeroIndex; i++) {
				buf.append(mantisdigits[i]);
			}
		}
		
		buf.append("p");
		if (exponentBits == 0) {
			buf.append("-");
			buf.append(Integer.toHexString(126));
		} else {
			int exponent = exponentBits-127;
			if (exponent < 0) {
				buf.append("-");
				buf.append(Integer.toHexString(-exponent));
			} else {
				buf.append(Integer.toHexString(exponent));
			}
		}
		
		return buf.toString();
	}
	
	private static float parseFromPositiveExactBinaryLiteral(String content) {
		int mantissSignIndex = -1;
		mantissSignIndex = content.indexOf('p');
		if (mantissSignIndex<0) {
			mantissSignIndex = content.indexOf('P');
		}
		if (mantissSignIndex<=0 || mantissSignIndex==content.length()-1) {
			throw new NumberFormatException(content);
		}
		String mantissStr = content.substring(0,mantissSignIndex);
		String exponentStr = content.substring(mantissSignIndex+1,content.length());
		
		boolean normalized = false;
		if (mantissStr.startsWith("1.")) {
			normalized = true;
		} else if (mantissStr.startsWith("0.")) {
			normalized = false;
		} else {
			throw new NumberFormatException(content);
		}
		
		mantissStr = mantissStr.substring(2,mantissStr.length());
		StringBuffer buf = new StringBuffer();
		buf.append(mantissStr);
		if (mantissStr.length()<6) {
			for (int i=0;i<6-mantissStr.length(); i++) {
				buf.append("0");
			}
		}
		mantissStr = buf.toString();
		
		int mantiss = Integer.parseInt(mantissStr, 16);
		mantiss = mantiss>>1;
		if (mantiss<0 || mantiss>0x7fffff) {
			throw new NumberFormatException(content);
		}
		
		int exponent = Integer.parseInt(exponentStr, 16);
		
		if (!normalized && exponent != -126) {
			throw new NumberFormatException(content);
		} else if (normalized) {
			exponent = exponent+127;
			if (exponent<=0 || exponent>254) {
				throw new NumberFormatException(content);
			}
		} else {
			exponent = 0;
		}
		
		
		float result =  Float.intBitsToFloat((exponent<<23)|mantiss);
		//System.err.println(content+":"+createExactHexLiteral(result));
		
		return result;
		
	}
	

}
