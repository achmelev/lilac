package org.jasm.item.instructions;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class InvokeInterfaceInstruction extends ConstantPoolInstruction {
	
	private short count = -1;

	public InvokeInterfaceInstruction(short opCode, InterfaceMethodrefInfo entry) {
		super(opCode, entry);
		if (entry !=null) {
			calculateCount(entry);
		}
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
		target.writeUnsignedByte(offset+3, (short)0);
	}
	
	private void calculateCount(InterfaceMethodrefInfo methodRef) {
		InterfaceMethodrefInfo iminfo = (InterfaceMethodrefInfo)cpEntry;
		MethodDescriptor descriptor = iminfo.getNameAndTypeReference().getMethodDescriptor();
		if (descriptor != null) {
			count = 1;
			for (TypeDescriptor t: descriptor.getParameters()) {
				if (t.isLong() || t.isDouble()) {
					count+=2;
				} else {
					count++;
				}
			}
		}
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
