package org.jasm.item.attribute;

import org.jasm.item.AbstractByteCodeItem;

public abstract class AbstractAttributeContent extends AbstractByteCodeItem {
	
	private int lengthToRead = -1;
	
	public void prepareRead(int length) {
		lengthToRead = length;
	}
	
	protected int getLengthToRead() {
		return lengthToRead;
	}
	
	@Override
	public String getPrintName() {
		return "attribute";
	}


}
