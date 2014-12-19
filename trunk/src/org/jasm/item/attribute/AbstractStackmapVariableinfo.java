package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractByteCodeItem;

public abstract class AbstractStackmapVariableinfo extends AbstractByteCodeItem {
	
	private short tag;
	
	public AbstractStackmapVariableinfo(short tag) {
		this.tag = tag;
	}
	
	@Override
	public void read(IByteBuffer source, long offset) {
		doReadBody(source, offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, tag);
		doWriteBody(target, offset);
	}
	
	protected abstract void doReadBody(IByteBuffer source, long offset);
	protected abstract void doWriteBody(IByteBuffer target, long offset);

	public short getTag() {
		return tag;
	}
	
	
	
	

}
