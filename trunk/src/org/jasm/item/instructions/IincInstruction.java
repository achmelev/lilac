package org.jasm.item.instructions;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.SymbolReference;

public class IincInstruction extends AbstractInstruction implements ILocalVariableReference {
	
	private SymbolReference localVariableReference = null;
	private int localVariableIndex = -1;
	private LocalVariable localVariable;
	private IntegerLiteral valueLiteral;
	private short value = -1;
	
	public IincInstruction(int localVariableIndex ,short value, boolean isWide) {
		super(OpCodes.iinc, isWide);
		this.localVariableIndex = localVariableIndex;
		this.value = value;
		if (!isWide && localVariableIndex>255) {
			throw new IllegalArgumentException(""+localVariableIndex);
		}
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
		return getAncestor(Instructions.class).getDissasemblingVarName(getLocalVariableReferences()[0])+", "+value;
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
		int minValue = isWide()?Short.MIN_VALUE:Byte.MIN_VALUE;
		int maxValue = isWide()?Short.MAX_VALUE:Byte.MAX_VALUE;
		LocalVariablesPool lvPool = ((Instructions)getParent()).getVariablesPool();
		LocalVariable var = lvPool.checkAndLoad(this,localVariableReference, 'i', false);
		if (var != null) {
			if (isWide() || var.getIndex()<=255) {
				this.localVariableIndex = var.getIndex();
				this.localVariable = var;
			} else {
				emitError(localVariableReference, "variable index is too big, consider to use the wide variant");
			}
		}
		
		Integer oI = valueLiteral.checkAndLoadValue(this);
		if (oI != null) {
			int iValue = oI.intValue();
			if (iValue<minValue || iValue>maxValue) {
				emitError(valueLiteral, "value out of bounds");
			} else {
				value = (short)iValue;
			}
		}
		
	}

	@Override
	public LocalVariable[] getLocalVariableReferences() {
		return new LocalVariable[]{new LocalVariable(localVariableIndex, JasmConsts.LOCAL_VARIABLE_TYPE_INT)};
	}


	public void setLocalVariableReference(SymbolReference localVariableReference) {
		this.localVariableReference = localVariableReference;
	}


	public void setValueLiteral(IntegerLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}


	public LocalVariable getLocalVariable() {
		return localVariable;
	}


	public short getValue() {
		return value;
	}
	
	
	

}
