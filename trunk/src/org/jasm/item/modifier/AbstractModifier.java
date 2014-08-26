package org.jasm.item.modifier;

import org.jasm.JasmConsts;

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
	
	protected boolean setFlag(String label, String flagLabel, int bit) {
		if (label.equals(flagLabel)) {
			value |= bit;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasNoFlags() {
		return toString().equals(JasmConsts.DEFAULT);
	}
	

}
