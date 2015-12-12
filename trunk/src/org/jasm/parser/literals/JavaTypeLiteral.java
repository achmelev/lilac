package org.jasm.parser.literals;

import org.apache.commons.lang3.StringUtils;
import org.jasm.type.descriptor.TypeDescriptor;

public class JavaTypeLiteral extends AbstractLiteral {
	
	private TypeDescriptor descriptor;

	public JavaTypeLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public TypeDescriptor getDescriptor() {
		if (descriptor == null) {
			return descriptor = createTypeDescriptor();
		}
		return descriptor;
	}
	
	private TypeDescriptor createTypeDescriptor() {
		String content = StringUtils.deleteWhitespace(getContent());
		return new TypeDescriptor(convertToDescriptorString(content));
	}
	
	private String convertToDescriptorString(String content) {
		if (content.startsWith("/")) {
			content = content.substring(1, content.length());
		}
		int arrayPartBegin = content.indexOf('[');
		String type;
		int arrayDimension = 0;
		if (arrayPartBegin<0) {
			type = content;
		} else {
			type = content.substring(0, arrayPartBegin);
			arrayDimension = (content.length()-arrayPartBegin)/2;
		}
		StringBuffer result = new StringBuffer();
		for (int i=0;i<arrayDimension; i++) {
			result.append("[");
		}
		result.append(mapJavaTypeToDescriptor(type));
		return result.toString();
	}
	
	private String mapJavaTypeToDescriptor(String type) {
		if (type.equals("byte")) {
			return "B";
		} else if (type.equals("char")) {
			return "C";
		} else if (type.equals("double")) {
			return "D";
		} else if (type.equals("float")) {
			return "F";
		} else if (type.equals("int")) {
			return "I";
		} else if (type.equals("long")) {
			return "J";
		} else if (type.equals("short")) {
			return "S";
		} else if (type.equals("boolean")) {
			return "Z";
		} else {
			return "L"+type+";";
		}
	}
	

}
