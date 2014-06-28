package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;

public class DoubleInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Double value = null;
	
	public DoubleInfo() {
		
	}
	
	public DoubleInfo(double value) {
		this.value = value;
	}

	@Override
	public short getTag() {
		return 6;
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
		value = source.readDouble(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeDouble(offset, value);
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public String getPrintName() {
		return "doubleinfo";
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
