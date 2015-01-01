package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.SymbolReference;

public class LocalVariableInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private int localVariableIndex = -1;
	private LocalVariable localVariable = null;
	private boolean forceNormal = false;
	private SymbolReference localVariableReference = null;
	
	private ShortLocalVariableInstruction shortReplacement = null;
	
	
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
		return getAncestor(Instructions.class).getDissasemblingVarName(getLocalVariableReferences()[0]);
	}
	
	

	@Override
	public String getPrintName() {
		if (isWide()) {
			return super.getPrintName();
		} else {
			if (localVariableIndex<=3 && getOpCode() != OpCodes.ret) {
				return "normal "+super.getPrintName(); 
			} else {
				return super.getPrintName();
			}
		}
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
			target.writeUnsignedShort(offset, localVariableIndex);
		} else {
			target.writeUnsignedByte(offset, (short)localVariableIndex);
		}
		
	}

	@Override
	protected void doResolve() {
		
	}
	
	@Override
	protected void doResolveAfterParse() {
		LocalVariablesPool lvPool = ((Instructions)getParent()).getVariablesPool();
		char type = OpCodes.getNameForOpcode(getOpCode()).charAt(0);
		LocalVariable var = lvPool.checkAndLoad(this,localVariableReference, type);
		if (var != null) {
			if (isWide() || var.getIndex()<=255) {
				this.localVariableIndex = var.getIndex();
				this.localVariable = var;
			} else {
				emitError(localVariableReference, "variable index is too big, consider to use the wide variant");
			}
		}
	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		char type = OpCodes.getNameForOpcode(getOpCode()).charAt(0);
		return new LocalVariable[]{new LocalVariable(localVariableIndex, type)};
	}


	public void setForceNormal(boolean forceNormal) {
		this.forceNormal = forceNormal;
	}


	public void setLocalVariableReference(SymbolReference localVariableReference) {
		this.localVariableReference = localVariableReference;
	}


	public SymbolReference getLocalVariableReference() {
		return localVariableReference;
	}
	
	public int getLocalVariableIndex() {
		return localVariableIndex;
	}


	public LocalVariable getLocalVariable() {
		return localVariable;
	}


	public ShortLocalVariableInstruction createShortReplacement() {
		if (shortReplacement != null) {
			return shortReplacement;
		} else {
			if ((localVariableIndex>=0 && localVariableIndex<=3) && !isWide() && !forceNormal && getOpCode() != OpCodes.ret) {
				short code = OpCodes.getOpcodeForName(OpCodes.getNameForOpcode(getOpCode())+"_"+localVariableIndex);
				shortReplacement = new ShortLocalVariableInstruction(code);
				return  shortReplacement;
			} else {
				return null;
			}
		}
		
	}
	

}
