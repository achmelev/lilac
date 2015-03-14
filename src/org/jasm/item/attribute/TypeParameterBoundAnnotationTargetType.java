package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;

public class TypeParameterBoundAnnotationTargetType extends AbstractAnnotationTargetType {
	
	private IntegerLiteral parameterIndexLiteral;
	private short parameterIndex = -1;
	private IntegerLiteral boundIndexLiteral;
	private short boundIndex = -1;

	public TypeParameterBoundAnnotationTargetType() {
		super();
	}


	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		parameterIndex = source.readUnsignedByte(offset+1);
		boundIndex = source.readUnsignedByte(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedByte(offset+1, parameterIndex);
		target.writeUnsignedByte(offset+2, boundIndex);
		
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
		StringBuffer buf = new StringBuffer();
		buf.append("targets type parameter bound");
		return buf.toString();
	}

	@Override
	public String getPrintArgs() {
		return parameterIndex+", "+boundIndex;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

	}
	
	

	@Override
	protected void doVerify() {
		
	}


	@Override
	protected void doResolveAfterParse() {
		
		if (isInMethod()) {
			setTargetType(JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER_BOUND);
		} else if (isInClass()) {
			setTargetType(JasmConsts.ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER_BOUND);
		} else {
			emitIllegalInContextError();
		}
		
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
		if (boundIndexLiteral.isValid()) {
			int iValue = boundIndexLiteral.getValue();
			if (iValue<0 || iValue>255) {
				emitError(boundIndexLiteral, "bound index out of bounds!");
			} else {
				boundIndex = (short)iValue;
			}
		} else {
			emitError(boundIndexLiteral, "malformed integer or integer out of bounds");
		}
	}

	public void setParameterIndexLiteral(IntegerLiteral parameterIndexLiteral) {
		this.parameterIndexLiteral = parameterIndexLiteral;
	}

	public void setBoundIndexLiteral(IntegerLiteral boundIndexLiteral) {
		this.boundIndexLiteral = boundIndexLiteral;
	}

	
	
}
