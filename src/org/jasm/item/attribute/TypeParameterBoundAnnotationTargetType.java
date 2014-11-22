package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class TypeParameterBoundAnnotationTargetType extends AbstractAnnotationTargetType {
	
	private short parameterIndex = -1;
	private short boundIndex = -1;

	public TypeParameterBoundAnnotationTargetType() {
		super();
	}

	public TypeParameterBoundAnnotationTargetType(short targetType, short parameterIndex, short boundIndex) {
		super(targetType);
		this.parameterIndex = parameterIndex;
		if (parameterIndex>=0 && parameterIndex<=255) {
			//OK
		} else {
			throw new IllegalArgumentException("parameter index out of bounds: "+parameterIndex);
		}
		this.boundIndex = boundIndex;
		if (boundIndex>=0 && boundIndex<=255) {
			//OK
		} else {
			throw new IllegalArgumentException("bound index out of bounds: "+boundIndex);
		}
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
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		buf.append("targets parameter type bound");
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
	protected void doResolveAfterParse() {
		
	}
	

}
