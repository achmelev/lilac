package org.jasm.type.descriptor;

import org.jasm.item.utils.IdentifierUtils;
import org.jasm.resolver.ExternalClassInfo;

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
	private ExternalClassInfo externalInfo;
	
	private String className = null;
	
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
			className = descriptor.substring(1,descriptor.length()-1);
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
			className = descriptor;
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
	
	public boolean isPrimitive() {
		return !isArray() && !isObject();
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

	public String getClassName() {
		return className;
	}

	public String getValue() {
		return descriptor;
	}
	
	public static TypeDescriptor parseFromStringBegin(String s) throws IllegalDescriptorException {
		if (s.length() == 0) {
			throw new IllegalDescriptorException("illegal descriptor "+s);
		}
		TypeDescriptor result;
		if (s.startsWith("B") || 
			s.startsWith("C") ||
			s.startsWith("D") ||
			s.startsWith("F") ||
			s.startsWith("I") ||
			s.startsWith("J") ||
			s.startsWith("S") ||
			s.startsWith("Z")) {
			return new TypeDescriptor(s.substring(0,1));
		} else if (s.startsWith("[")) {
			if (s.length()==1) {
				throw new IllegalDescriptorException("illegal type descriptor begin "+s);
			} else {
				try {
					TypeDescriptor d1 = TypeDescriptor.parseFromStringBegin(s.substring(1,s.length()));
					return new TypeDescriptor("["+d1.getValue());
				} catch (IllegalDescriptorException e) {
					throw new IllegalDescriptorException("illegal type descriptor begin "+s);
				}
			}
		} else if (s.startsWith("L")) {
			if (s.indexOf(';')>2) {
				String s1 = s.substring(0,s.indexOf(';')+1);
				try {
					result = new TypeDescriptor(s1);
				} catch (IllegalDescriptorException e) {
					throw new IllegalDescriptorException("illegal type descriptor begin "+s);
				}
				
			} else {
				throw new IllegalDescriptorException("illegal type descriptor begin "+s);
			}
		} else {
			throw new IllegalDescriptorException(s);
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descriptor == null) ? 0 : descriptor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeDescriptor other = (TypeDescriptor) obj;
		if (descriptor == null) {
			if (other.descriptor != null)
				return false;
		} else if (!descriptor.equals(other.descriptor))
			return false;
		return true;
	}

	public ExternalClassInfo getExternalInfo() {
		if (isObject || isArray) {
			return externalInfo;
		} else {
			throw new IllegalStateException("cannot set external info");
		}
	}

	public void setExternalInfo(ExternalClassInfo externalInfo) {
		if (isObject || isArray) {
			this.externalInfo = externalInfo;
		} else {
			throw new IllegalStateException("cannot set external info");
		}
	}
	
	

}
