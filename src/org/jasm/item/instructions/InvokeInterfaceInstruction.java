package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;

public class InvokeInterfaceInstruction extends ConstantPoolInstruction {
	
	private short count = -1;

	public InvokeInterfaceInstruction(short opCode, InterfaceMethodrefInfo entry) {
		super(opCode, entry);
	}

	@Override
	public int getLength() {
		return super.getLength()+2;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		super.read(source, offset);
		count = source.readUnsignedByte(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		super.write(target, offset);
		target.writeUnsignedByte(offset+2, count);
	}
	
	private void calculateCount(InterfaceMethodrefInfo methodRef) {
		//TODO
	}
	
	

	
	

}
