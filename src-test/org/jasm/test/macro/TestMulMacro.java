package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;

public class TestMulMacro extends AbstractMacro {

	@Override
	public boolean resolve() {
		boolean result = true;
		if (getNumberOfArguments() >=2) {
			for (int i=0;i<getNumberOfArguments(); i++) {
				IMacroArgument arg = getArgument(i);
				if (isString(arg) || isFloatingPoint(arg)) {
					emitError(arg.getSourceLocation(), "unexpected argument type");
					result = false;
				} else if (isInteger(arg)) {
					long value = getIntegerValue(arg);
					if (value<Integer.MIN_VALUE || value>Integer.MAX_VALUE) {
						emitError(arg.getSourceLocation(), "argument value out of bounds");
						result = false;
					}
					
				}
			}
		} else {
			emitError(null, "too little arguments");
			result = false;
		}
		return result;
	}

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		
		for (int i=0;i<getNumberOfArguments(); i++) {
			IMacroArgument arg = getArgument(i);
			if (isInteger(arg)) {
				pushIntArgument(arg, result);
			}
		}
		
		for (int i=0;i<getNumberOfArguments()-1; i++) {
			result.add(createArgumentLessInstruction(OpCodes.imul));
		}
		
		return result;
	}
	
	private void pushIntArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		long value = getIntegerValue(arg);
		if (value<=Byte.MAX_VALUE && value<=Byte.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else if (value<=Short.MAX_VALUE && value<=Short.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else {
			IntegerInfo ii = getIntegerInfo((int)value);
			result.add(createLdcInstruction(ii));
		}
	}
	
	

}
