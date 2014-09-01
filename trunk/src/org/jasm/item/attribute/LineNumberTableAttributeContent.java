package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class LineNumberTableAttributeContent extends
		AbstractBytecodeItemList<LineNumber> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "line number table";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected LineNumber createEmptyItem(IByteBuffer source, long offset) {
		return new LineNumber();
	}

}
