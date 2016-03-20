package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.SymbolReference;

public class BranchInstruction extends AbstractInstruction implements IReferencingInstruction {
	
	
	private int targetOffset = -1;
	private SymbolReference targetReference;
	private AbstractInstruction targetInst = null;

	private boolean isWide;
	
	private boolean afterTarget;
		
	public BranchInstruction(short opCode, AbstractInstruction inst, boolean wide) {
		super(opCode, false);
		this.targetInst = inst;
		this.isWide = wide;
	}
	
	public BranchInstruction(short opCode, AbstractInstruction inst) {
		super(opCode, false);
		if (OpCodes.isBranchInstruction(opCode)) {
			isWide = false;
		} else if (OpCodes.isWideBranchInstruction(opCode)) {
			isWide = true;
		} else {
			throw new IllegalArgumentException(Integer.toHexString(opCode));
		}
		targetInst = inst;
	}
	
	public BranchInstruction(short opCode, SymbolReference targetReference) {
		super(opCode, false);
		if (OpCodes.isBranchInstruction(opCode)) {
			isWide = false;
		} else if (OpCodes.isWideBranchInstruction(opCode)) {
			isWide = true;
		} else {
			throw new IllegalArgumentException(Integer.toHexString(opCode));
		}
		this.targetReference = targetReference;
	}

	@Override
	public int getLength() {
		if (isWide) {
			return 5;
		} else {
			return 3;
		}
		
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
		return targetInst.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		if (!isWide) {
			targetOffset = source.readShort(offset);
		} else {
			targetOffset = source.readInt(offset);
		}
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		if (!isWide) {
			target.writeShort(offset, (short)(targetInst.getOffsetInCode()-this.getOffsetInCode()));
		} else {
			target.writeInt(offset, (targetInst.getOffsetInCode()-this.getOffsetInCode()));
		}
	}

	@Override
	protected void doResolve() {
		targetInst = getInstructions().getInstructionAtOffset(this.getOffsetInCode()+targetOffset);
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{targetInst};
	}
	
	@Override
	protected void doResolveAfterParse() {
		Instructions instrs = (Instructions)getParent();
		if (targetReference != null) {
			this.targetInst = instrs.checkAndLoadFromSymbolTable(this, targetReference);
		} 
		if (afterTarget) {
			int index = instrs.indexOf(targetInst);
			if (index == instrs.getSize()-1) {
				emitError(null, "no instruction after the target");
			} else {
				targetInst = instrs.get(index+1);
			}
		}
		
	}

	public void setTargetReference(SymbolReference targetReference) {
		this.targetReference = targetReference;
	}

	public AbstractInstruction getTargetInst() {
		return targetInst;
	}
	
	public void replaceLocalVarInstructonsWithShortVersions() {
		if (targetInst instanceof LocalVariableInstruction) {
			LocalVariableInstruction localVarInstr = (LocalVariableInstruction)targetInst;
			ShortLocalVariableInstruction shortV = localVarInstr.createShortReplacement();
			if (shortV != null) {
				targetInst = shortV;
			}
		}
	}

	@Override
	protected void doVerify() {
		int offset = targetInst.getOffsetInCode()-this.getOffsetInCode();
		if (!isWide() && (offset>Short.MAX_VALUE || offset<Short.MIN_VALUE)) {
			emitError(targetReference, "The target too far. Consider to use the wide modifier");
		}
	}

	public void setAfterTarget(boolean afterTarget) {
		this.afterTarget = afterTarget;
	}
	
	

}
