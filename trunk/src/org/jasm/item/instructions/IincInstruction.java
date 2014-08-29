package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class IincInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private int localVariableIndex = -1;
	private short value = -1;
	
	public IincInstruction(int localVariableIndex ,short value, boolean isWide) {
		super(OpCodes.iinc, isWide);
		this.localVariableIndex = localVariableIndex;
		this.value = value;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		if (isWide()) {
			localVariableIndex = source.readUnsignedShort(offset);
			value = source.readShort(offset+2);
		} else {
			localVariableIndex = source.readUnsignedByte(offset);
			value = source.readByte(offset+1);
		}
		

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		if (this.isWide()) {
			target.writeUnsignedShort(offset, localVariableIndex);
			target.writeShort(offset+2, value);
		} else {
			target.writeUnsignedByte(offset, (short)localVariableIndex);
			target.writeByte(offset+1, (byte)value);
		}
		
	}

	@Override
	public int getLength() {
		return this.isWide()?6:3;
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
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		return new LocalVariable[]{new LocalVariable(localVariableIndex, JasmConsts.LOCAL_VARIABLE_TYPE_INT)};
	}

}
