package org.jasm.item.descriptor;

import org.jasm.item.utils.IdentifierUtils;

public class TypeDescriptor {
	
	private boolean isByte;
	private boolean isCharacter;
	private boolean isDouble;
	private boolean isFloat;
	private boolean isInteger;
	private boolean isLong;
	private boolean isObject;
	private boolean isArray;
	private boolean isShort;
	private boolean isBoolean;
	
	private TypeDescriptor componentType;
	
	private String descriptor;
	
	public TypeDescriptor(String descriptor) throws IllegalDescriptorException {
		this.descriptor = descriptor;
		isByte = false;
		isCharacter = false;
		isDouble = false;
		isFloat = false;
		isInteger = false;
		isLong = false;
		isObject = false;
		isArray = false;
		isShort = false;
		isBoolean = false;
		
		if (descriptor.equals("B")) {
			isByte = true;
		} else if (descriptor.equals("C")) {
			isCharacter = true;
		} else if (descriptor.equals("D")) {
			isDouble = true;
		} else if (descriptor.equals("F")) {
			isFloat = true;
		} else if (descriptor.equals("I")) {
			isInteger = true;
		} else if (descriptor.equals("J")) {
			isLong = true;
		} else if (descriptor.startsWith("L") & descriptor.endsWith(";")) {
			String className = descriptor.substring(1,descriptor.length()-1);
			if (IdentifierUtils.isValidJasmClassName(className)) {
				isObject = true;
			} else {
				throw new IllegalDescriptorException("illegal type descriptor: "+className);
			}
			
		} else if (descriptor.startsWith("[")) {
			isArray = true;
			if (descriptor.length()<2) {
				throw new IllegalDescriptorException("illegal type descriptor: "+descriptor);
			}
			componentType = new TypeDescriptor(descriptor.substring(1, descriptor.length()));
		} else if (descriptor.equals("S")) {
			isShort = true;
		} else if (descriptor.equals("Z")) {
			isBoolean = true;
		} else {
			throw new IllegalDescriptorException(("illegal type descriptor: "+descriptor));
		}
		
	}

	public boolean isByte() {
		return isByte;
	}

	public boolean isCharacter() {
		return isCharacter;
	}

	public boolean isDouble() {
		return isDouble;
	}

	public boolean isFloat() {
		return isFloat;
	}
	
	public boolean isInteger() {
		return isInteger;
	}

	public boolean isLong() {
		return isLong;
	}

	public boolean isObject() {
		return isObject;
	}

	public boolean isArray() {
		return isArray;
	}

	public boolean isShort() {
		return isShort;
	}

	public boolean isBoolean() {
		return isBoolean;
	}

	public TypeDescriptor getComponentType() {
		if (!isArray()) {
			throw new IllegalStateException("isn't an array!");
		}
		return componentType;
	}

	@Override
	public String toString() {
		return descriptor;
	}
	
	
	

}
