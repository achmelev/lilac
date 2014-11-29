package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.item.instructions.Instructions;

public class TypeArgumentAnnotationTargetType extends AbstractAnnotationTargetType implements IInstructionReference {
	
	private int instructionIndex = -1;
	private AbstractInstruction instruction;
	private short parameterIndex = -1;

	public TypeArgumentAnnotationTargetType() {
		super();
	}

	public TypeArgumentAnnotationTargetType(short targetType, AbstractInstruction instruction,short parameterIndex) {
		super(targetType);
		this.parameterIndex = parameterIndex;
		if (parameterIndex>=0 && parameterIndex<=255) {
			//OK
		} else {
			throw new IllegalArgumentException("index out of bounds: "+parameterIndex);
		}
		this.instruction = instruction;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		instructionIndex = source.readUnsignedShort(offset+1);
		parameterIndex = source.readUnsignedByte(offset+3);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, instruction.getOffsetInCode());
		target.writeUnsignedByte(offset+3, parameterIndex);
		
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		if (targetType == JasmConsts.ANNOTATION_TARGET_CAST) {
			buf.append("targets cast type");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT) {
			buf.append("targets constructor type argument");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT_IN_METHOD_REF) {
			buf.append("targets constructor reference type argument");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT) {
			buf.append("targets method type argument");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF) {
			buf.append("targets method reference type argument");
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
		return instruction.getPrintLabel()+", "+parameterIndex;
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