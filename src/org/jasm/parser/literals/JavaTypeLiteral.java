package org.jasm.parser.literals;

import org.apache.commons.lang3.StringUtils;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.type.descriptor.TypeDescriptor;

public class JavaTypeLiteral extends AbstractLiteral {
	
	private AbstractByteCodeItem parent;
	
	private TypeDescriptor descriptor;
	
	private boolean resolveError = false;

	public JavaTypeLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public TypeDescriptor getDescriptor() {
		if (descriptor == null && !resolveError) {
			return descriptor = createTypeDescriptor();
		}
		return descriptor;
	}
	
	private TypeDescriptor createTypeDescriptor() {
		String content = StringUtils.deleteWhitespace(getContent());
		String descriptorString = convertToDescriptorString(content);
		if (!resolveError) {
			return new TypeDescriptor(descriptorString);
		} else {
			return null;
		}
	}
	
	private String convertToDescriptorString(String content) {
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
		String descriptorString = mapJavaTypeToDescriptor(type);
		if (descriptorString == null) {
			return null;
		}
		result.append(descriptorString);
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
			if (type.indexOf('/')>=0) {
				if (type.startsWith("/")) {
					type = type.substring(1, type.length());
				}
				return "L"+type+";";
			} else {
				SymbolReference ref = new SymbolReference(this.getLine(), this.getCharPosition(), type);
				ClassInfo info = parent.getConstantPool().checkAndLoadFromSymbolTable(parent, ClassInfo.class, ref);
				if (info != null) {
					return "L"+info.getClassName()+";";
				} else {
					resolveError = true;
					return null;
				}
			}
			
		}
	}

	public void setParent(AbstractByteCodeItem parent) {
		this.parent = parent;
	}

	
	
	
	

}
