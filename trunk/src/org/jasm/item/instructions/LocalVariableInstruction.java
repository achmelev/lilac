package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class LocalVariableInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private int localVariableIndex = -1;
	
	
	public LocalVariableInstruction(short opCode,boolean isWide, int localVariableIndex) {
		super(opCode, isWide);
		this.localVariableIndex = localVariableIndex;
		if (!isWide && localVariableIndex>255) {
			throw new IllegalArgumentException(""+localVariableIndex);
		}
		
	}
	

	@Override
	public int getLength() {
		return this.isWide()?4:2;
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
		char type = getPrintName().charAt(0);
		return type+"loc"+localVariableIndex;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		if (this.isWide()) {
			this.localVariableIndex = source.readUnsignedShort(offset);
		} else {
			this.localVariableIndex = source.readUnsignedByte(offset);
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		if (this.isWide()) {
			target.writeUnsignedShort(offset, (short)localVariableIndex);
		} else {
			target.writeUnsignedByte(offset, (short)localVariableIndex);
		}
		
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
		char type = OpCodes.getNameForOpcode(getOpCode()).charAt(0);
		return new LocalVariable[]{new LocalVariable(localVariableIndex, type)};
	}
	
	

}
