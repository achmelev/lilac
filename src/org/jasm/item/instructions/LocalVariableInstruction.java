package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class LocalVariableInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private short localVariableIndex = -1;
	
	
	public LocalVariableInstruction(short opCode, short localVariableIndex) {
		super(opCode);
		this.localVariableIndex = localVariableIndex;
		
	}

	@Override
	public int getLength() {
		return 2;
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
	public String getPrintArgs() {
		return "loc"+localVariableIndex;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.localVariableIndex = source.readUnsignedByte(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, localVariableIndex);
	}

	@Override
	protected void doResolve() {
		
	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		char type = getPrintName().charAt(0);
		return new LocalVariable[]{new LocalVariable(localVariableIndex, type)};
	}
	
	

}
