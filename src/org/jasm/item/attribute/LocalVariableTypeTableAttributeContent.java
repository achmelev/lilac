package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class LocalVariableTypeTableAttributeContent extends
		AbstractBytecodeItemList<LocalVariableType> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "debug variable types";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected LocalVariableType createEmptyItem(IByteBuffer source, long offset) {
		return new LocalVariableType();
	}

}
