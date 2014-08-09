package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class BranchInstruction extends AbstractInstruction implements IInstructionReference {
	
	
	private int targetOffset = -1;
	private AbstractInstruction targetInst = null;
	
	
	
	public BranchInstruction(short opCode, AbstractInstruction inst) {
		super(opCode);
		this.targetInst = inst;
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
		return targetInst.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetOffset = source.readShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeShort(offset, (short)(targetInst.getOffsetInCode()-this.getOffsetInCode()));
	}

	@Override
	protected void doResolve() {
		targetInst = getInstructions().getInstructionAtOffset(this.getOffsetInCode()+targetOffset);
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{targetInst};
	}

}
