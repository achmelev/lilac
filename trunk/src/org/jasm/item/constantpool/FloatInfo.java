package org.jasm.item.constantpool;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;

public class FloatInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
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
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
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
	public String getTypeLabel() {
		return  "const float";
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
