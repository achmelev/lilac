package org.jasm.type.descriptor;

import java.util.ArrayList;
import java.util.List;

public class MethodDescriptor {
	
	private List<TypeDescriptor> parameters = new ArrayList<>();
	private TypeDescriptor returnType;
	
	private String value = null;
	
	public MethodDescriptor(String descriptor) throws IllegalDescriptorException {
		this.value = descriptor;
		if (!descriptor.startsWith("(")) {
			throw new IllegalDescriptorException("illegal method descriptor "+descriptor);
		} else {
			int index = descriptor.indexOf(')');
			if (index < 0 || index == descriptor.length()-1) {
				throw new IllegalDescriptorException("illegal method descriptor "+descriptor);
			} else {
				try {
					String paramsString = descriptor.substring(1,index);
					String returnTypeString = descriptor.substring(index+1, descriptor.length());
					parseParameters(paramsString);
					parseReturnType(returnTypeString);
				} catch (IllegalDescriptorException e) {
					throw new IllegalDescriptorException("illegal method descriptor "+descriptor);
				}
			}
		}
	}
	
	public MethodDescriptor(List<TypeDescriptor> params, TypeDescriptor returnType) {
		if (params != null) {
			parameters.addAll(params);
		}
		this.returnType = returnType;
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		for (TypeDescriptor t: parameters) {
			buf.append(t.getValue());
		}
		buf.append(")");
		if (returnType != null) {
			buf.append(returnType.getValue());
		} else {
			buf.append("V");
		}
		value = buf.toString();
	}
	
	private void parseParameters(String s) throws IllegalDescriptorException {
		String s1 = s;
		while (s1.length()>0) {
			TypeDescriptor t = TypeDescriptor.parseFromStringBegin(s1);
			parameters.add(t);
			s1 = s1.substring(t.getValue().length(),s1.length());
		}
	}
	
	private void parseReturnType(String s) throws IllegalDescriptorException {
		if (s.equals("V")) {
			return;
		} else {
			returnType = new TypeDescriptor(s);
		}
	}

	public List<TypeDescriptor> getParameters() {
		return parameters;
	}

	public TypeDescriptor getReturnType() {
		return returnType;
	}

	public String getValue() {
		return value;
	}
	
	public int calculateParamsLength() {
		int result = 0;
		for (TypeDescriptor td: parameters) {
			result+=td.getTypeSize();
		}
		return result;
	}
	
	public boolean isVoid() {
		return (returnType == null);
	}
	

}
