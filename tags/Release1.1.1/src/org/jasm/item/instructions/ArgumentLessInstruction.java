package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ArgumentLessInstruction extends AbstractInstruction  {
	
	public ArgumentLessInstruction() {
		
	}
	
	public ArgumentLessInstruction(short opCode) {
		super(opCode);
		if (!OpCodes.isArgumentLessInstruction(opCode)) {
			throw new IllegalArgumentException("Unknown opcode: "+Integer.toHexString(opCode));
		}
	}
	
	@Override
	public int getLength() {
		return 1;
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
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {

	}

	@Override
	public void write(IByteBuffer target, long offset) {

	}

	@Override
	protected void doResolve() {

	}
	
	@Override
	protected void doResolveAfterParse() {
		
	}

	public boolean isReturn() {
		return opCode == OpCodes.areturn ||
			   opCode == OpCodes.dreturn ||
			   opCode == OpCodes.freturn ||
			   opCode == OpCodes.ireturn ||
			   opCode == OpCodes.lreturn ||
			   opCode == OpCodes.return_;
	}

}
