package org.jasm.type.descriptor;

import java.util.ArrayList;
import java.util.List;

public class MethodDescriptor {
	
	private List<TypeDescriptor> parameters = new ArrayList<>();
	private TypeDescriptor returnType;
	
	public MethodDescriptor(String descriptor) throws IllegalDescriptorException {
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
	
	private void parseParameters(String s) throws IllegalDescriptorException {
		String s1 = s;
		while (s1.length()>0) {
			TypeDescriptor t = TypeDescriptor.parseFromStringBegin(s1);
			parameters.add(t);
			s1 = s1.substring(t.getDescriptor().length(),s1.length());
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
	
	

}
