package org.jasm.item.constantpool;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;

public class IntegerInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Integer value = null;
	
	public IntegerInfo() {
		
	}
	
	public IntegerInfo(int value) {
		this.value = value;
	}

	@Override
	public short getTag() {
		return 3;
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
		value = source.readInt(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeInt(offset, value);
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	@Override
	public String getPrintName() {
		return "integerinfo";
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
