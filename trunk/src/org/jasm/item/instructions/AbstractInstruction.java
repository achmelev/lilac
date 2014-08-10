package org.jasm.item.instructions;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.attribute.CodeAttributeContent;

public abstract class AbstractInstruction extends AbstractByteCodeItem {

	private short opCode = -1;
	private int offsetInCode = -1;
	
	public AbstractInstruction() {
		
	}
	
	public AbstractInstruction(short opCode) {
		this.opCode = opCode;
	}
	

	public short getOpCode() {
		return opCode;
	}

	public void setOpCode(short opCode) {
		this.opCode = opCode;
	}

	public int getOffsetInCode() {
		return offsetInCode;
	}

	public void setOffsetInCode(int offsetInCode) {
		this.offsetInCode = offsetInCode;
	}
	
	protected CodeAttributeContent getCode() {
		Instructions instr = (Instructions)getParent();
		if (instr == null) {
			throw new IllegalStateException("Orphan instruction");
		}
		CodeAttributeContent code = (CodeAttributeContent)instr.getParent();
		if (code == null) {
			throw new IllegalStateException("Orphan instructions");
		}
		
		return code;
	}
	
	protected Instructions getInstructions() {
		Instructions instr = (Instructions)getParent();
		if (instr == null) {
			throw new IllegalStateException("Orphan instruction");
		}
		return instr;
	}

	@Override
	public String getPrintLabel() {
		Instructions instr = (Instructions)getParent();
		if (instr.getReferencingItems(this).size() > 0) {
			return "ir"+this.getOffsetInCode();
		} else {
			return null;
		}
	}

	@Override
	public String getPrintName() {
		return OpCodes.getNameForOpcode(opCode);
	}

	
	

}
