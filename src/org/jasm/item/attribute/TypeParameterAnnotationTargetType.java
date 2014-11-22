package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class TypeParameterAnnotationTargetType extends AbstractAnnotationTargetType {
	
	private short index = -1;

	public TypeParameterAnnotationTargetType() {
		super();
	}

	public TypeParameterAnnotationTargetType(short targetType, short index) {
		super(targetType);
		this.index = index;
		if (index>=0 && index<=255) {
			//OK
		} else {
			throw new IllegalArgumentException("index out of bounds: "+index);
		}
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
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		buf.append("targets parameter type");
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
	protected void doResolveAfterParse() {
		
	}
	

}
