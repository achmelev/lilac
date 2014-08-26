package org.jasm.item.modifier;

public abstract class AbstractModifier {
	
	protected int value = -1;
	
	public AbstractModifier(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	protected boolean append(StringBuffer buf, boolean flag,boolean comma, String word) {
		if (flag) {
			if (comma) {
				buf.append(", ");
			}
			buf.append(word);
		}
		return flag;
	}

}
