package org.jasm.item.modifier;

import org.jasm.JasmConsts;

public class InnerClassModifier extends AbstractModifier {
	
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
	private static int INTERFACE_BIT=0x0200;
	private static String INTERFACE_LABEL="interface";
	private static int ABSTRACT_BIT=0x0400;
	private static String ABSTRACT_LABEL="abstract";
	private static int SYNTETIC_BIT=0x1000;
	private static String SYNTETIC_LABEL="syntetic";
	private static int ANNOTATION_BIT=0x2000;
	private static String ANNOTATION_LABEL="annotation";
	private static int ENUM_BIT=0x4000;
	private static String ENUM_LABEL="enum";
	
	
	public InnerClassModifier(int value) {
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
	
	
	public boolean isInterface() {
		return (this.value & 0x0200) !=0;
	}
	
	public boolean isAbstract() {
		return (this.value & INTERFACE_BIT) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & SYNTETIC_BIT) !=0;
	}
	
	public boolean isAnnotation() {
		return (this.value & ANNOTATION_BIT) !=0;
	}
	
	public boolean isEnum() {
		return (this.value & ENUM_BIT) !=0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, PUBLIC_LABEL) || komma;
		komma = append(buf, isProtected(), komma, PROTECTED_LABEL) || komma;
		komma = append(buf, isPrivate(), komma, PRIVATE_LABEL) || komma;
		komma = append(buf, isStatic(), komma, STATIC_LABEL) || komma;
		komma = append(buf, isFinal(), komma, FINAL_LABEL) || komma;
		komma = append(buf, isInterface(), komma, INTERFACE_LABEL) || komma;
		komma = append(buf, isAbstract(), komma, ABSTRACT_LABEL) || komma;
		komma = append(buf, isSyntetic(), komma, SYNTETIC_LABEL) || komma;
		komma = append(buf, isAnnotation(), komma, ANNOTATION_LABEL) || komma;
		komma = append(buf, isEnum(), komma, ENUM_LABEL) || komma;
		String result =  buf.toString();
		if (result.length() == 0) {
			return JasmConsts.DEFAULT;
		} else {
			return result;
		}
		
	}
	
	public void setFlag(String label) {
		boolean result = false;
		result = result || setFlag(label, PUBLIC_LABEL, PUBLIC_BIT);
		result = result || setFlag(label, PROTECTED_LABEL, PROTECTED_BIT);
		result = result || setFlag(label, PRIVATE_LABEL, PRIVATE_BIT);
		result = result || setFlag(label, STATIC_LABEL, STATIC_BIT);
		result = result || setFlag(label, FINAL_LABEL, FINAL_BIT);
		result = result || setFlag(label, ABSTRACT_LABEL, ABSTRACT_BIT);
		result = result || setFlag(label, INTERFACE_LABEL, INTERFACE_BIT);
		result = result || setFlag(label, SYNTETIC_LABEL, SYNTETIC_BIT);
		result = result || setFlag(label, ANNOTATION_LABEL, ANNOTATION_BIT);
		result = result || setFlag(label, ENUM_LABEL, ENUM_BIT);
		if (!result) {
			throw new IllegalArgumentException("Illegal modifier label: "+label);
		}
	}
	
	

}
