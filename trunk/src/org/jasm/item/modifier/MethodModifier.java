package org.jasm.item.modifier;

public class MethodModifier extends AbstractClassMemberModifier {
	
	private int value = -1;
	
	public MethodModifier(int value) {
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
	
	public boolean isSynchronized() {
		return (this.value & 0x0020) !=0;
	}
	
	public boolean isBridge() {
		return (this.value & 0x0040) !=0;
	}
	
	public boolean isVarargs() {
		return (this.value & 0x0080) !=0;
	}
	
	public boolean isNative() {
		return (this.value & 0x0100) !=0;
	}
	
	public boolean isAbstract() {
		return (this.value & 0x0400) !=0;
	}
	
	public boolean isStrict() {
		return (this.value & 0x0800) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & 0x1000) !=0;
	}
	
	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, "public") || komma;
		komma = append(buf, isPrivate(), komma, "private") || komma;
		komma = append(buf, isProtected(), komma, "protected") || komma;
		komma = append(buf, isStatic(), komma, "static") || komma;
		komma = append(buf, isFinal(), komma, "final") || komma;
		komma = append(buf, isSynchronized(), komma, "synchronized") || komma;
		komma = append(buf, isBridge(), komma, "bridge") || komma;
		komma = append(buf, isVarargs(), komma, "varargs") || komma;
		komma = append(buf, isNative(), komma, "native") || komma;
		komma = append(buf, isAbstract(), komma, "abstract") || komma;
		komma = append(buf, isStrict(), komma, "strict") || komma;
		komma = append(buf, isSyntetic(), komma, "syntetic") || komma;
		String result =  buf.toString();
		if (result.length() == 0) {
			return "default";
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
