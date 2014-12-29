package org.jasm.item.modifier;

import org.jasm.JasmConsts;

public class MethodModifier extends AbstractClassMemberModifier implements MemberModifier {
	
	private static int PUBLIC_BIT=0x0001;
	private static String PUBLIC_LABEL="public";
	private static int PRIVATE_BIT=0x0002;
	private static String PRIVATE_LABEL="private";
	private static int PROTECTED_BIT=0x0004;
	private static String PROTECTED_LABEL="protected";
	private static int STATIC_BIT=0x0008;
	private static String STATIC_LABEL="static";
	private static int FINAL_BIT=0x0010;
	private static String FINAL_LABEL="final";
	private static int SYNCHRONIZED_BIT=0x0020;
	private static String SYNCHRONIZED_LABEL="synchronized";
	private static int BRIDGE_BIT=0x0040;
	private static String BRIDGE_LABEL="bridge";
	private static int VARARGS_BIT=0x0080;
	private static String VARARGS_LABEL="varargs";
	private static int NATIVE_BIT=0x0100;
	private static String NATIVE_LABEL="native";
	private static int ABSTRACT_BIT=0x0400;
	private static String ABSTRACT_LABEL="abstract";
	private static int STRICT_BIT=0x0800;
	private static String STRICT_LABEL="strict";
	private static int SYNTETIC_BIT=0x1000;
	private static String SYNTETIC_LABEL="synthetic";
	
	
	public MethodModifier(int value) {
		super(value);
	}
	
	public boolean isPublic() {
		return (this.value & PUBLIC_BIT) !=0;
	}
	
	public boolean isPrivate() {
		return (this.value & PRIVATE_BIT) !=0;
	}
	
	public boolean isProtected() {
		return (this.value & PROTECTED_BIT) !=0;
	}
	
	public boolean isStatic() {
		return (this.value & STATIC_BIT) !=0;
	}
	
	
	public boolean isFinal() {
		return (this.value & FINAL_BIT) !=0;
	}
	
	public boolean isSynchronized() {
		return (this.value & SYNCHRONIZED_BIT) !=0;
	}
	
	public boolean isBridge() {
		return (this.value & BRIDGE_BIT) !=0;
	}
	
	public boolean isVarargs() {
		return (this.value & VARARGS_BIT) !=0;
	}
	
	public boolean isNative() {
		return (this.value & NATIVE_BIT) !=0;
	}
	
	public boolean isAbstract() {
		return (this.value & ABSTRACT_BIT) !=0;
	}
	
	public boolean isStrict() {
		return (this.value & STRICT_BIT) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & SYNTETIC_BIT) !=0;
	}
	
	public void setFlag(String label) {
		boolean result = false;
		result = result || setFlag(label, PUBLIC_LABEL, PUBLIC_BIT);
		result = result || setFlag(label, PRIVATE_LABEL, PRIVATE_BIT);
		result = result || setFlag(label, PROTECTED_LABEL, PROTECTED_BIT);
		result = result || setFlag(label, ABSTRACT_LABEL, ABSTRACT_BIT);
		result = result || setFlag(label, STATIC_LABEL, STATIC_BIT);
		result = result || setFlag(label, FINAL_LABEL, FINAL_BIT);
		result = result || setFlag(label, SYNCHRONIZED_LABEL, SYNCHRONIZED_BIT);
		result = result || setFlag(label, VARARGS_LABEL, VARARGS_BIT);
		result = result || setFlag(label, BRIDGE_LABEL, BRIDGE_BIT);
		result = result || setFlag(label, NATIVE_LABEL, NATIVE_BIT);
		result = result || setFlag(label, STRICT_LABEL, STRICT_BIT);
		result = result || setFlag(label, SYNTETIC_LABEL, SYNTETIC_BIT);
		if (!result) {
			throw new IllegalArgumentException("Illegal modifier label: "+label);
		}
	}
	
	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, PUBLIC_LABEL) || komma;
		komma = append(buf, isPrivate(), komma, PRIVATE_LABEL) || komma;
		komma = append(buf, isProtected(), komma, PROTECTED_LABEL) || komma;
		komma = append(buf, isStatic(), komma, STATIC_LABEL) || komma;
		komma = append(buf, isFinal(), komma, FINAL_LABEL) || komma;
		komma = append(buf, isSynchronized(), komma, SYNCHRONIZED_LABEL) || komma;
		komma = append(buf, isBridge(), komma, BRIDGE_LABEL) || komma;
		komma = append(buf, isVarargs(), komma, VARARGS_LABEL) || komma;
		komma = append(buf, isNative(), komma, NATIVE_LABEL) || komma;
		komma = append(buf, isAbstract(), komma, ABSTRACT_LABEL) || komma;
		komma = append(buf, isStrict(), komma, STRICT_LABEL) || komma;
		komma = append(buf, isSyntetic(), komma, SYNTETIC_LABEL) || komma;
		String result =  buf.toString();
		if (result.length() == 0) {
			return JasmConsts.DEFAULT;
		} else {
			return result;
		}
		
	}
	
	
	

}
