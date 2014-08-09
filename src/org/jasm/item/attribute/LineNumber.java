package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.Instructions;

public class LineNumber extends AbstractByteCodeItem {
	
	private int startPC = -1;
	AbstractInstruction startInstruction = null;
	private int lineNumber = -1;
	
	public LineNumber() {
		
	}
	
	public LineNumber(AbstractInstruction startInstruction) {
		this.startInstruction = startInstruction;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		startPC = source.readUnsignedShort(offset);
		lineNumber = source.readUnsignedShort(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		Instructions instr = ((CodeAttributeContent)getParent().getParent().getParent().getParent()).getInstructions();
		target.writeUnsignedShort(offset, startInstruction.getOffsetInCode());
		target.writeUnsignedShort(offset+2, lineNumber);
	}

	@Override
	public int getLength() {
		return 4;
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
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "line number";
	}

	@Override
	public String getPrintArgs() {
		return startInstruction.getOffsetInCode()+", "+lineNumber;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		Instructions instr = ((CodeAttributeContent)getParent().getParent().getParent().getParent()).getInstructions();
		startInstruction = instr.getInstructionAtOffset(startPC);
	}

}
