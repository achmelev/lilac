package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;

public class BipushInstruction extends AbstractPushInstruction {
	
	private byte value = -1;
	private IntegerLiteral valueLiteral;
	
	public BipushInstruction(byte value) {
		super(OpCodes.bipush);
		this.value = value;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.value = source.readByte(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeByte(offset, value);
	}

	@Override
	public int getLength() {
		return 2;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintArgs() {
		return ""+value;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

	}
	

	public void setValue(byte value) {
		this.value = value;
	}
	
	@Override
	protected void setInValue(int ivalue) {
		value = (byte)ivalue;
		
	}

	@Override
	protected int getMinInValue() {
		return Byte.MIN_VALUE;
	}

	@Override
	protected int getMaxInValue() {
		return Byte.MAX_VALUE;
	}

	public byte getValue() {
		return value;
	}
	
	

}
