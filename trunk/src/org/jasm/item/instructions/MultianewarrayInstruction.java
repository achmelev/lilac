package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.ClassInfo;

public class MultianewarrayInstruction extends ConstantPoolInstruction {
	
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

	
	
	

}
