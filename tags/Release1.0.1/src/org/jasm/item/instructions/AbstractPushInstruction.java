package org.jasm.item.instructions;

import org.jasm.parser.literals.IntegerLiteral;

public abstract class AbstractPushInstruction extends AbstractInstruction {

	private IntegerLiteral valueLiteral;
	
	public AbstractPushInstruction(short opCode) {
		super(opCode);
	}
	
	
	@Override
	protected void doResolveAfterParse() {
		Integer oI = valueLiteral.checkAndLoadValue(this);
		if (oI != null) {
			int iValue = oI.intValue();
			if (iValue<getMinInValue() || iValue>getMaxInValue()) {
				emitError(valueLiteral, "value out of bounds");
			} else {
				setInValue(iValue);
			}
		}
		
	}
	
	protected abstract void setInValue(int value);
	protected abstract int getMinInValue();
	protected abstract int getMaxInValue();


	public void setValueLiteral(IntegerLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}
	
	
	

}
