package org.jasm.item.modifier;

public class ClassModifier {
	
	private int value = -1;
	
	public ClassModifier(int value) {
		this.value = value;
	}
	
	public boolean isPublic() {
		return (this.value & 0x0001) !=0;
	}
	
	public boolean isFinal() {
		return (this.value & 0x0010) !=0;
	}
	
	public boolean isSuper() {
		return (this.value & 0x0020) !=0;
	}
	
	public boolean isInterface() {
		return (this.value & 0x0200) !=0;
	}
	
	public boolean isAbstract() {
		return (this.value & 0x0400) !=0;
	}
	
	public boolean isSyntetic() {
		return (this.value & 0x1000) !=0;
	}
	
	public boolean isAnnotation() {
		return (this.value & 0x2000) !=0;
	}
	
	public boolean isEnum() {
		return (this.value & 0x4000) !=0;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean komma = false;
		komma = append(buf, isPublic(), komma, "public") || komma;
		komma = append(buf, isFinal(), komma, "final") || komma;
		komma = append(buf, isSuper(), komma, "super") || komma;
		komma = append(buf, isInterface(), komma, "interface") || komma;
		komma = append(buf, isAbstract(), komma, "abstract") || komma;
		komma = append(buf, isSyntetic(), komma, "syntetic") || komma;
		komma = append(buf, isAnnotation(), komma, "annotation") || komma;
		komma = append(buf, isEnum(), komma, "enum") || komma;
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
