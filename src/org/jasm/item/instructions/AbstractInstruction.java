package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.literals.Label;
import org.jasm.type.verifier.VerifierParams;

public abstract class AbstractInstruction extends AbstractByteCodeItem implements ISymbolTableEntry {

	private short opCode = -1;
	private boolean isWide = false;
	
	private Label label = null;
	
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
			int result = 0;
			for (int i=0;i<index; i++) {
				result+=instr.get(i).getLength();
			}
			return result;
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
		return getSymbolName();
	}

	@Override
	public String getPrintName() {
		return (this.isWide?"wide ":"")+OpCodes.getNameForOpcode(opCode);
	}

	@Override
	protected void doResolve() {
		
	}
	
	
	@Override
	protected void doVerify(VerifierParams params) {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		
	}

	public boolean isWide() {
		return isWide;
	}
	
	

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public String getSymbolName() {
		if (label == null) {
			return createInstructionLabel();
		}
		return label.getLabel();
		
	}


	private String createInstructionLabel() {
		Instructions instr = (Instructions)getParent();
		if (instr.getReferencingItems(this).size() > 0) {
			return "ir"+this.getOffsetInCode();
		} else {
			return null;
		}
	}
	

}
