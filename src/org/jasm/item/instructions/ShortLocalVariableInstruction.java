package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ShortLocalVariableInstruction extends AbstractInstruction implements ILocalVariableReference, IRegisterIndexInstruction {
	
	private short localVariableIndex = -1;
	
	
	public ShortLocalVariableInstruction(short opCode) {
		super(opCode);
		this.localVariableIndex = getVariableIndex();
		if (!OpCodes.isShortLocalVariableInstruction(opCode)) {
			throw new IllegalArgumentException("Unknown opcode: "+Integer.toHexString(opCode));
		}
		
	}

	@Override
	public int getLength() {
		return 1;
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
		return getAncestor(Instructions.class).getDissasemblingVarName(getLocalVariableReferences()[0]);
				
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		
	}

	@Override
	protected void doResolve() {
		this.localVariableIndex = getVariableIndex();
	}
	
	@Override
	protected void doResolveAfterParse() {
		
	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		char type = getPrintName().charAt(0);
		return new LocalVariable[]{new LocalVariable(localVariableIndex, type)};
	}
	
	@Override
	public String getPrintName() {
		return getInstructionName();
	}
	
	private String getInstructionName() {
		String name = OpCodes.getNameForOpcode(getOpCode());
		return name.substring(0,name.indexOf("_"));
	}
	
	private short getVariableIndex() {
		String name = OpCodes.getNameForOpcode(getOpCode());
		
		return new Integer(name.substring(name.indexOf("_")+1,name.length())).shortValue();
	}

	@Override
	public int getRegisterIndex() {
		return localVariableIndex;
	}
	
}
