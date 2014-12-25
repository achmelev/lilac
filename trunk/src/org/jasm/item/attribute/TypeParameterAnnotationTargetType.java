package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.type.verifier.VerifierParams;

public class TypeParameterAnnotationTargetType extends AbstractAnnotationTargetType {
	
	private IntegerLiteral indexLiteral; 
	private short index = -1;
	

	public TypeParameterAnnotationTargetType() {
		super();
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		index = source.readUnsignedByte(offset+1);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedByte(offset+1, index);
		
	}

	@Override
	public int getLength() {
		return 2;
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
		buf.append("targets type parameter");
		return buf.toString();
	}

	@Override
	public String getPrintArgs() {
		return ""+index;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

	}
	
	@Override
	protected void doVerify(VerifierParams params) {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (isInMethod()) {
			setTargetType(JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER);
		} else if (isInClass()) {
			setTargetType(JasmConsts.ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER);
		} else {
			emitIllegalInContextError();
		}
		
		if (indexLiteral.isValid()) {
			int iValue = indexLiteral.getValue();
			if (iValue<0 || iValue>255) {
				emitError(indexLiteral, "parameter index out of bounds!");
			} else {
				index = (short)iValue;
			}
		} else {
			emitError(indexLiteral, "malformed integer or integer out of bounds");
		}
		
	}

	public void setIndexLiteral(IntegerLiteral indexLiteral) {
		this.indexLiteral = indexLiteral;
	}
	
	
	
	
}
