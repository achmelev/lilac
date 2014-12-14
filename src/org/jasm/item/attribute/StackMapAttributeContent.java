package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class StackMapAttributeContent extends AbstractBytecodeItemList<AbstractStackmapFrame> {

	@Override
	public String getPrintName() {
		return "stackmap";
	}

	@Override
	protected AbstractStackmapFrame createEmptyItem(IByteBuffer source,
			long offset) {
		return null;
	}

}
