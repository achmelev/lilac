package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class EmptyAnnotationTargetType extends AbstractAnnotationTargetType {
	
	

	public EmptyAnnotationTargetType() {
		super();
	}

	public EmptyAnnotationTargetType(short targetType) {
		super(targetType);
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		
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
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		StringBuffer buf = new StringBuffer();
		buf.append("targets ");
		if (targetType == JasmConsts.ANNOTATION_TARGET_FIELD) {
			buf.append("field type");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_RECEIVER_TYPE) {
			buf.append("receiver type");
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_RETURN_TYPE) {
			buf.append("return type");
		} else {
			throw new IllegalStateException("Unknown target type: "+Integer.toHexString(targetType));
		}
		
		return buf.toString();
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

		
	}
	
	

	@Override
	protected void doVerify() {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (!
				(
						(isInMethod() && 
								(JasmConsts.ANNOTATION_TARGET_RETURN_TYPE==targetType
								|| JasmConsts.ANNOTATION_TARGET_RECEIVER_TYPE==targetType))
				|| (isInField() && JasmConsts.ANNOTATION_TARGET_FIELD==targetType)
				)
			) {
			emitIllegalInContextError();
		}
	}
	

}
