package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;

public class LongInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Long value = null;
	
	public LongInfo() {
		
	}
	
	public LongInfo(long value) {
		this.value = value;
	}

	@Override
	public short getTag() {
		return 5;
	}

	@Override
	public int getLength() {
		return 9;
	}

	@Override
	protected void doResolve() {
		

	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		value = source.readLong(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeLong(offset, value);
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	@Override
	public String getPrintName() {
		return "longinfo";
	}

	@Override
	public String getPrintArgs() {
		return value.toString();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

}
