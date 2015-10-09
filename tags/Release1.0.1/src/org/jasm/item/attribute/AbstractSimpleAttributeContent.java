package org.jasm.item.attribute;

import org.jasm.item.AbstractByteCodeItem;

public abstract class AbstractSimpleAttributeContent extends AbstractByteCodeItem implements IAttributeContent {
	
	private int lengthToRead = -1;
	
	public void prepareRead(int length) {
		lengthToRead = length;
	}
	
	protected int getLengthToRead() {
		return lengthToRead;
	}
	


}
