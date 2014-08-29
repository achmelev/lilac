package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class SipushInstruction extends AbstractInstruction {
	
	private short value = -1;
	
	public SipushInstruction(short value) {
		super(OpCodes.sipush);
		this.value = value;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.value = source.readShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeShort(offset, value);
	}

	@Override
	public int getLength() {
		return 3;
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
	
	@Override
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}
}
