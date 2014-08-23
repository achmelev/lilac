package org.jasm.item.instructions;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.attribute.CodeAttributeContent;

public abstract class AbstractInstruction extends AbstractByteCodeItem {

	private short opCode = -1;
	private boolean isWide = false;
	
	public AbstractInstruction() {
		
	}
	
	public AbstractInstruction(short opCode, boolean isWide) {
		this.opCode = opCode;
		this.isWide = isWide;
	}
	
	public AbstractInstruction(short opCode) {
		this(opCode, false);
	}
	

	public short getOpCode() {
		return opCode;
	}

	public void setOpCode(short opCode) {
		this.opCode = opCode;
	}

	public int getOffsetInCode() {
		Instructions instr = (Instructions)getParent();
		int index = instr.indexOf(this);
		if (index > 0) {
			AbstractInstruction prev = instr.get(index-1);
			return prev.getOffsetInCode()+prev.getLength();
		} else {
			return 0;
		}
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
		return (this.isWide?"wide ":"")+OpCodes.getNameForOpcode(opCode);
	}

	public boolean isWide() {
		return isWide;
	}

	
	

}
