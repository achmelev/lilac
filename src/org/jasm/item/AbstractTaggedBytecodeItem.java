package org.jasm.item;

import org.jasm.bytebuffer.IByteBuffer;

public abstract class AbstractTaggedBytecodeItem implements ITaggedBytecodeItem {


	@Override
	public void read(IByteBuffer source, long offset) {
		short tagValue = source.readShort(offset);
		if (tagValue != getTag()) {
			throw new IllegalArgumentException("Expected "+getTag()+" but got "+tagValue);
		}
		read(source, offset+1);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, getTag());
		writeBody(target, offset+1);

	}
	
	public abstract void readBody(IByteBuffer source, long offset);
	public abstract void writeBody(IByteBuffer target, long offset);

		

}
