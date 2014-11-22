package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ThrowsAnnotationTargetType extends AbstractAnnotationTargetType implements IThrowsDeclarationsReference {
	
	private int index = -1;

	public ThrowsAnnotationTargetType() {
		super();
	}

	public ThrowsAnnotationTargetType(short targetType, short index) {
		super(targetType);
		this.index = index;
		if (index>=0 && index<=65535) {
			//OK
		} else {
			throw new IllegalArgumentException("index out of bounds: "+index);
		}
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		index = source.readUnsignedShort(offset+1);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, index);
		
	}

	@Override
	public int getLength() {
		return 3;
	}

	@Override
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		buf.append("targets exception");
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
		if (index != JasmConsts.ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX) {
			return "throwsref_"+index;
		} else {
			return null;
		}
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

		
	}

	@Override
	protected void doResolveAfterParse() {
		
	}

	@Override
	public int[] getIndexes() {
		return new int[]{index};
	}
	

}
