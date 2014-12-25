package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.item.instructions.Instructions;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.verifier.VerifierParams;

public class TypeArgumentAnnotationTargetType extends AbstractAnnotationTargetType implements IInstructionReference {
	
	private SymbolReference instructionReference;
	private int instructionIndex = -1;
	private AbstractInstruction instruction;
	private IntegerLiteral parameterIndexLiteral;
	private short parameterIndex = -1;

	public TypeArgumentAnnotationTargetType() {
		super();
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
	protected void doVerify(VerifierParams params) {
		
	}


	@Override
	protected void doResolveAfterParse() {
		if (isInCode()) {
			
			CodeAttributeContent content = getAncestor(CodeAttributeContent.class);
			instruction = content.getInstructions().checkAndLoadFromSymbolTable(this, instructionReference);
			
			if (parameterIndexLiteral.isValid()) {
				int iValue = parameterIndexLiteral.getValue();
				if (iValue<0 || iValue>255) {
					emitError(parameterIndexLiteral, "parameter index out of bounds!");
				} else {
					parameterIndex = (short)iValue;
				}
			} else {
				emitError(parameterIndexLiteral, "malformed integer or integer out of bounds");
			}
			
		} else {
			emitIllegalInContextError();
		}
		
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{instruction};
	}

	public void setInstructionReference(SymbolReference instructionReference) {
		this.instructionReference = instructionReference;
	}

	public void setParameterIndexLiteral(IntegerLiteral parameterIndexLiteral) {
		this.parameterIndexLiteral = parameterIndexLiteral;
	}

	

}
