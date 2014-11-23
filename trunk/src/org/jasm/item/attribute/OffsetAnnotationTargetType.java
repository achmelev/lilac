package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.item.instructions.Instructions;

public class OffsetAnnotationTargetType extends AbstractAnnotationTargetType implements IInstructionReference {
	
	private int instructionIndex = -1;
	private AbstractInstruction instruction;

	public OffsetAnnotationTargetType() {
		super();
	}

	public OffsetAnnotationTargetType(short targetType, AbstractInstruction instruction) {
		super(targetType);
		this.instruction = instruction;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		instructionIndex = source.readUnsignedShort(offset+1);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, instruction.getOffsetInCode());
		
	}

	@Override
	public int getLength() {
		return 3;
	}

	@Override
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		if (targetType == JasmConsts.ANNOTATION_TARGET_NEW) {
			buf.append("targets new type");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_INSTANCEOF) {
			buf.append("targets instanceof type");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_METHOD_REF_ID) {
			buf.append("targets method reference");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_METHOD_REF_NEW) {
			buf.append("targets constructor reference");
		} else {
			throw new IllegalArgumentException("unknown target type: "+Integer.toHexString(targetType));
		}
		
		return buf.toString();
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
		return getTypeLabel();
	}

	@Override
	public String getPrintArgs() {
		return instruction.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		Instructions instrs = ((CodeAttributeContent)getAncestor(CodeAttributeContent.class)).getInstructions();
		instruction = instrs.getInstructionAtOffset(instructionIndex);
	}

	@Override
	protected void doResolveAfterParse() {
		
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{instruction};
	}

	

}
