package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class LocalVariableTableAttributeContent extends
		AbstractBytecodeItemList<DebugLocalVariable> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "debug vars";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected DebugLocalVariable createEmptyItem(IByteBuffer source, long offset) {
		return new DebugLocalVariable();
	}

}
