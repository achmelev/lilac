package org.jasm.item.modifier;

import org.jasm.JasmConsts;

public class FieldModifier extends AbstractClassMemberModifier {
	
	private int value = -1;
	
	public FieldModifier(int value) {
		this.value = value;
	}
	
	public boolean isPublic() {
		return (this.value & 0x0001) !=0;
	}
	
	public boolean isPrivate() {
		return (this.value & 0x0002) !=0;
	}
	
	public boolean isProtected() {
		return (this.value & 0x0004) !=0;
	}
	
	public boolean isStatic() {
		return (this.value & 0x0008) !=0;
	}
	
	
	public boolean isFinal() {
		return (this.value & 0x0010) !=0;
	}
	
	public boolean isVolatile() {
		return (this.value & 0x0040) !=0;
	}
	
	public boolean isTransient() {
		return (this.value & 0x0080) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & 0x1000) !=0;
	}
	
	
	public boolean isEnum() {
		return (this.value & 0x4000) !=0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, "public") || komma;
		komma = append(buf, isPrivate(), komma, "private") || komma;
		komma = append(buf, isProtected(), komma, "protected") || komma;
		komma = append(buf, isStatic(), komma, "static") || komma;
		komma = append(buf, isFinal(), komma, "final") || komma;
		komma = append(buf, isVolatile(), komma, "volatile") || komma;
		komma = append(buf, isTransient(), komma, "transient") || komma;
		komma = append(buf, isSyntetic(), komma, "syntetic") || komma;
		komma = append(buf, isEnum(), komma, "enum") || komma;
		String result =  buf.toString();
		if (result.length() == 0) {
			return JasmConsts.DEFAULT;
		} else {
			return result;
		}
		
	}
	
	private boolean append(StringBuffer buf, boolean flag,boolean comma, String word) {
		if (flag) {
			if (comma) {
				buf.append(", ");
			}
			buf.append(word);
		}
		return flag;
	}

	public int getValue() {
		return value;
	}
	
	

}
