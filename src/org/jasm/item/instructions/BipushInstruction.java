package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class BipushInstruction extends AbstractInstruction {
	
	private byte value = -1;
	
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

}
