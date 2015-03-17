package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.environment.Environment;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IDebugInstructionReference;
import org.jasm.item.instructions.Instructions;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.SymbolReference;

public class LineNumber extends AbstractByteCodeItem implements IDebugInstructionReference {
	
	private int startPC = -1;
	private SymbolReference startInstructionLabel;
	AbstractInstruction startInstruction = null;
	private IntegerLiteral lineNumberLiteral;
	private int lineNumber = -1;
	
	public LineNumber() {
		
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		startPC = source.readUnsignedShort(offset);
		lineNumber = source.readUnsignedShort(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
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
		return "line";
	}

	@Override
	public String getPrintArgs() {
		return startInstruction.getPrintLabel()+", "+lineNumber;
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
	
	
	
	@Override
	protected void doVerify() {
		
		
	}


	@Override
	protected void doResolveAfterParse() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		startInstruction = code.getInstructions().checkAndLoadFromSymbolTable(this, startInstructionLabel);
		Integer oI = lineNumberLiteral.checkAndLoadValue(this);
		if (oI != null && oI>0 && oI<65536) {
			lineNumber = oI;
		} else {
			emitError(lineNumberLiteral, "line number out of bounds");
		}
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{startInstruction};
	}

	public void setStartInstructionLabel(SymbolReference startInstructionLabel) {
		this.startInstructionLabel = startInstructionLabel;
	}

	public void setLineNumberLiteral(IntegerLiteral lineNumberLiteral) {
		this.lineNumberLiteral = lineNumberLiteral;
	}

	public AbstractInstruction getStartInstruction() {
		return startInstruction;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	
	
	

}
