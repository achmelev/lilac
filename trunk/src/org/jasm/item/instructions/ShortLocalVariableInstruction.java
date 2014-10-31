package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class ShortLocalVariableInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private short localVariableIndex = -1;
	
	
	public ShortLocalVariableInstruction(short opCode) {
		super(opCode);
		this.localVariableIndex = getInstructionIndex();
		
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
		char type = getPrintName().charAt(0);
		return type+"_"+localVariableIndex;
				
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
		this.localVariableIndex = getInstructionIndex();
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
	
	private short getInstructionIndex() {
		String name = OpCodes.getNameForOpcode(getOpCode());
		
		return new Integer(name.substring(name.indexOf("_")+1,name.length())).shortValue();
	}
	
}
