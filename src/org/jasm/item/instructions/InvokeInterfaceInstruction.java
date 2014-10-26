package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.descriptor.MethodDescriptor;

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
		InterfaceMethodrefInfo iminfo = (InterfaceMethodrefInfo)cpEntry;
		String descriptor = iminfo.getNameAndTypeReference().getDescriptor();
		count = (short)(new MethodDescriptor(descriptor).getParameters().size()+1);
	}

	@Override
	protected void doResolveAfterParse() {
		super.doResolveAfterParse();
		InterfaceMethodrefInfo iminfo = (InterfaceMethodrefInfo)cpEntry;
		if (iminfo != null) {
			calculateCount(iminfo);
		}
	}

	public short getCount() {
		return count;
	}
	
	
	

}
