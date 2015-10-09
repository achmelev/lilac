package org.jasm.parser.literals;

public class DoubleLiteral extends AbstractLiteral {

	public DoubleLiteral(int line, int charPosition, String content) {
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
	
	public double getValue() {
		String content = getContent();
		char sign='+';
		double result = -1;
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
	
	public static String createExactHexLiteral(double value) {
		
		if (Double.isNaN(value) || value == Double.NEGATIVE_INFINITY || value == Double.POSITIVE_INFINITY) {
			throw new NumberFormatException(""+value);
		}
		
		long bits = Double.doubleToLongBits(value);
		
		long signBit = (bits>>63)&0x1L;
		long exponentBits = (bits>>52)&0x7ffL;
		long mantissBits = bits & 0xfffffffffffffL;
		
		StringBuffer buf = new StringBuffer();
		if (signBit == 1) {
			buf.append("-");
		}
		if (exponentBits == 0) {
			buf.append("0.");
		} else {
			buf.append("1.");
		}
		
		String[] mantisdigits = new String[13];
		
		for (int i=0;i<13; i++) {
			mantisdigits[i] = Long.toHexString((mantissBits>>(48-i*4))&0xFL);
		}
		
		
		int lastNonZeroIndex = 12;
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
			buf.append(Integer.toHexString(1022));
		} else {
			long exponent = exponentBits-1023l;
			if (exponent < 0) {
				buf.append("-");
				buf.append(Long.toHexString(-exponent));
			} else {
				buf.append(Long.toHexString(exponent));
			}
		}
		
		return buf.toString();
	}
	
	private static double parseFromPositiveExactBinaryLiteral(String content) {
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
		if (mantissStr.length()<13) {
			for (int i=0;i<13-mantissStr.length(); i++) {
				buf.append("0");
			}
		}
		mantissStr = buf.toString();
		
		long mantiss = Long.parseLong(mantissStr, 16);
		if (mantiss<0 || mantiss>0xfffffffffffffL) {
			throw new NumberFormatException(content);
		}
		
		long exponent = Long.parseLong(exponentStr, 16);
		
		if (!normalized && exponent != -1022) {
			throw new NumberFormatException(content);
		} else if (normalized) {
			exponent = exponent+1023;
			if (exponent<=0 || exponent>2047) {
				throw new NumberFormatException(content);
			}
		} else {
			exponent = 0;
		}
		
		
		long bits = (exponent<<52)|mantiss;
		
		
		double result = Double.longBitsToDouble(bits);
		//System.err.println(content+":"+createExactHexLiteral(result));
		
		return result;
		
	}
	

}
