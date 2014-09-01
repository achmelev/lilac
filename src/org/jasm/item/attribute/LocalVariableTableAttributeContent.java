package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class LocalVariableTableAttributeContent extends
		AbstractBytecodeItemList<LocalVariable> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "local variable table";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected LocalVariable createEmptyItem(IByteBuffer source, long offset) {
		return new LocalVariable();
	}

}
