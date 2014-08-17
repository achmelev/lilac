package org.jasm.item.instructions;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class IincInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private short localVariableIndex = -1;
	private byte value = -1;
	
	public IincInstruction(short localVariableIndex, byte value) {
		super(OpCodes.iinc);
		this.localVariableIndex = localVariableIndex;
		this.value = value;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		localVariableIndex = source.readUnsignedByte(offset);
		value = source.readByte(offset+1);

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, localVariableIndex);
		target.writeByte(offset+1, value);
	}

	@Override
	public int getLength() {
		return 3;
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
		char type = JasmConsts.LOCAL_VARIABLE_TYPE_INT;
		return type+"loc"+localVariableIndex;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		

	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		return new LocalVariable[]{new LocalVariable(localVariableIndex, JasmConsts.LOCAL_VARIABLE_TYPE_INT)};
	}

}
