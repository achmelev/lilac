package org.jasm.item.constantpool;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.literals.LongLiteral;

public class LongInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Long value = null;
	private LongLiteral valueLiteral = null;
	
	public LongInfo() {
		
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
		if (valueLiteral.isValid()) {
			value = new Long(valueLiteral.getValue());
		} else {
			emitError(valueLiteral, "malformed integer or integer out of bounds");
		}
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
	public String getConstTypeLabel() {
		return  "long";
	}

	@Override
	public String getPrintArgs() {
		return value.toString();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	public LongLiteral getValueLiteral() {
		return valueLiteral;
	}

	public void setValueLiteral(LongLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}

	@Override
	protected String doGetDisassemblerLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
