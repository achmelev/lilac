package org.jasm.item.modifier;

import org.jasm.JasmConsts;

public class ClassModifier extends AbstractModifier {
	
	private static int PUBLIC_BIT=0x0001;
	private static String PUBLIC_LABEL="public";
	private static int FINAL_BIT=0x0010;
	private static String FINAL_LABEL="final";
	private static int SUPER_BIT=0x0020;
	private static String SUPER_LABEL="super";
	private static int INTERFACE_BIT=0x0200;
	private static String INTERFACE_LABEL="interface";
	private static int ABSTRACT_BIT=0x0400;
	private static String ABSTRACT_LABEL="abstract";
	private static int SYNTETIC_BIT=0x1000;
	private static String SYNTETIC_LABEL="synthetic";
	private static int ANNOTATION_BIT=0x2000;
	private static String ANNOTATION_LABEL="annotation";
	private static int ENUM_BIT=0x4000;
	private static String ENUM_LABEL="enum";
	
	
	public ClassModifier(int value) {
		super(value);
	}
	
	public boolean isPublic() {
		return (this.value & PUBLIC_BIT) !=0;
	}
	
	public boolean isFinal() {
		return (this.value & FINAL_BIT) !=0;
	}
	
	public boolean isSuper() {
		return (this.value & SUPER_BIT) !=0;
	}
	
	public boolean isInterface() {
		return (this.value & INTERFACE_BIT) !=0;
	}
	
	public boolean isAbstract() {
		return (this.value & ABSTRACT_BIT) !=0;
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
		komma = append(buf, isFinal(), komma, FINAL_LABEL) || komma;
		komma = append(buf, isSuper(), komma, SUPER_LABEL) || komma;
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
		result = result || setFlag(label, FINAL_LABEL, FINAL_BIT);
		result = result || setFlag(label, SUPER_LABEL, SUPER_BIT);
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
