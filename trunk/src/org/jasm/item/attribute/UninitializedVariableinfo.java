package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.parser.literals.SymbolReference;

import com.sun.prism.impl.Disposer.Target;

public class UninitializedVariableinfo extends AbstractStackmapVariableinfo implements IInstructionReference {
	
	private int instructionOffset;
	private AbstractInstruction instruction;
	private SymbolReference instructionReference;

	public UninitializedVariableinfo() {
		super((short)8);
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
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "uninitialized "+instruction.getPrintLabel();
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
	protected void doResolve() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		instruction = code.getInstructions().getInstructionAtOffset(instructionOffset);
	}

	@Override
	protected void doResolveAfterParse() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		instruction = code.getInstructions().checkAndLoadFromSymbolTable(this, instructionReference);
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		instructionOffset = source.readUnsignedShort(offset+1);
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, instruction.getOffsetInCode());
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{instruction};
	}

}
