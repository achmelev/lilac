package org.jasm.item.modifier;

import org.jasm.JasmConsts;


public class FieldModifier extends AbstractClassMemberModifier {
	
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
	private static int VOLATILE_BIT=0x0040;
	private static String VOLATILE_LABEL="volatile";
	private static int TRANSIENT_BIT=0x0080;
	private static String TRANSIENT_LABEL="transient";
	private static int SYNTETIC_BIT=0x1000;
	private static String SYNTETIC_LABEL="synthetic";
	private static int ENUM_BIT=0x4000;
	private static String ENUM_LABEL="enum";
	
	
	public FieldModifier(int value) {
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
	
	public boolean isVolatile() {
		return (this.value & VOLATILE_BIT) !=0;
	}
	
	public boolean isTransient() {
		return (this.value & TRANSIENT_BIT) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & SYNTETIC_BIT) !=0;
	}
	
	
	public boolean isEnum() {
		return (this.value & ENUM_BIT) !=0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, PUBLIC_LABEL) || komma;
		komma = append(buf, isPrivate(), komma, PRIVATE_LABEL) || komma;
		komma = append(buf, isProtected(), komma, PROTECTED_LABEL) || komma;
		komma = append(buf, isStatic(), komma, STATIC_LABEL) || komma;
		komma = append(buf, isFinal(), komma, FINAL_LABEL) || komma;
		komma = append(buf, isVolatile(), komma, VOLATILE_LABEL) || komma;
		komma = append(buf, isTransient(), komma, TRANSIENT_LABEL) || komma;
		komma = append(buf, isSyntetic(), komma, SYNTETIC_LABEL) || komma;
		komma = append(buf, isEnum(), komma, ENUM_LABEL) || komma;
		String result =  buf.toString();
		if (result.length() == 0) {
			return JasmConsts.DEFAULT;
		} else {
			return result;
		}
		
	}

	@Override
	public void setFlag(String label) {
		boolean result = false;
		result = result || setFlag(label, PUBLIC_LABEL, PUBLIC_BIT);
		result = result || setFlag(label, PRIVATE_LABEL, PRIVATE_BIT);
		result = result || setFlag(label, PROTECTED_LABEL, PROTECTED_BIT);
		result = result || setFlag(label, STATIC_LABEL, STATIC_BIT);
		result = result || setFlag(label, FINAL_LABEL, FINAL_BIT);
		result = result || setFlag(label, VOLATILE_LABEL, VOLATILE_BIT);
		result = result || setFlag(label, TRANSIENT_LABEL, TRANSIENT_BIT);
		result = result || setFlag(label, SYNTETIC_LABEL, SYNTETIC_BIT);
		result = result || setFlag(label, ENUM_LABEL, ENUM_BIT);
		if (!result) {
			throw new IllegalArgumentException("Illegal modifier label: "+label);
		}
		
	}
	

	
	

}
