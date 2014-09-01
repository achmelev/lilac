package org.jasm.item.constantpool;

import org.apache.commons.lang3.NotImplementedException;
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
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
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
	public String getTypeLabel() {
		return  "const long";
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
