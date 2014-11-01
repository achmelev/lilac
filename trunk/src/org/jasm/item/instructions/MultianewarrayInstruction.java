package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.parser.literals.IntegerLiteral;

public class MultianewarrayInstruction extends ConstantPoolInstruction {
	
	private IntegerLiteral dimensionsLiteral;
	private short dimensions = -1;
	
	public MultianewarrayInstruction() {
		super(OpCodes.multianewarray, null);
	}

	public MultianewarrayInstruction(ClassInfo entry, short dimensions) {
		super(OpCodes.multianewarray, entry);
		this.dimensions = dimensions;
		if (dimensions < 1) {
			throw new IllegalArgumentException(dimensions+"");
		}
	}

	@Override
	public String getPrintArgs() {
		return super.getPrintArgs();
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		super.read(source, offset);
		dimensions = source.readUnsignedByte(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		super.write(target, offset);
		target.writeUnsignedByte(offset+2, dimensions);
	}

	@Override
	public int getLength() {
		return super.getLength()+1;
	}

	@Override
	protected void doResolveAfterParse() {
		super.doResolveAfterParse();
		int iValue = dimensionsLiteral.getValue();
		if (iValue<1 || iValue>255) {
			emitError(dimensionsLiteral, "dimensions value out of bounds");
		} else {
			dimensions = (short)iValue;
		}
	}

	public void setDimensionsLiteral(IntegerLiteral dimensionsLiteral) {
		this.dimensionsLiteral = dimensionsLiteral;
	}

	public short getDimensions() {
		return dimensions;
	}

	public ClassInfo getClassInfo() {
		return (ClassInfo)cpEntry;
	}
	
	

}
