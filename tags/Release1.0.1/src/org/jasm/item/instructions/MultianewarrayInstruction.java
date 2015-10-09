package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.resolver.ExternalClassInfo;

public class MultianewarrayInstruction extends ConstantPoolInstruction {
	
	private IntegerLiteral dimensionsLiteral;
	private short dimensions = -1;
	
	public MultianewarrayInstruction() {
		super(OpCodes.multianewarray, null);
	}

	@Override
	public String getPrintArgs() {
		return super.getPrintArgs()+", "+dimensions;
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
		if (dimensionsLiteral.isValid()) {
			int iValue = dimensionsLiteral.getValue();
			if (iValue<1 || iValue>255) {
				emitError(dimensionsLiteral, "dimensions value out of bounds");
			} else {
				dimensions = (short)iValue;
			}
		} else {
			emitError(dimensionsLiteral, "malformed integer or integer out of bounds");
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

	@Override
	protected void verifyInstructions() {
		ExternalClassInfo cli = (ExternalClassInfo)info;
		if (!cli.isArray()) {
			emitError(cpEntryReference, "the argument must be an array type");
		}
		int dim = cli.getDescriptor().getArrayDimension();
		if (dim<dimensions) {
			emitError(dimensionsLiteral, "inconsistent array dimensions");
		}
	}
	
	
	
	

}
