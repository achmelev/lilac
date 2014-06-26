package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;

public class FloatInfo extends AbstractConstantPoolEntry {
	
	private Float value = null;
	
	public FloatInfo() {
		
	}
	
	public FloatInfo(float value) {
		this.value = value;
	}

	@Override
	public short getTag() {
		return 4;
	}

	@Override
	public int getLength() {
		return 5;
	}

	@Override
	protected void doResolve() {
		

	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		value = source.readFloat(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeFloat(offset, value);
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String getPrintName() {
		return "floatinfo";
	}

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}
	
	

}
