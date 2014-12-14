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
		short value = source.readUnsignedByte(offset);
		if (value>=0 && value<=63) {
			return new SameStackmapFrame();
		} else if (value>=248 && value<=250) {
			return new ChopStackmapFrame();
		} else {
			throw new IllegalArgumentException("unknown tag value: "+value);
		}
	}

}
