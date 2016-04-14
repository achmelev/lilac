package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestMulMacro extends AbstractMacro {

	@Override
	public boolean doResolve() {
		boolean result = true;
		if (getNumberOfArguments() >=2) {
			for (int i=0;i<getNumberOfArguments(); i++) {
				IMacroArgument arg = getArgument(i);
				TypeDescriptor type = getArgumentType(arg);
				if (type == null || !type.isInteger()) {
					emitError(arg.getSourceLocation(), "wrong argument type");
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
			pushArgument(getArgument(i), result);
		}
		
		for (int i=0;i<getNumberOfArguments()-1; i++) {
			result.add(createArgumentLessInstruction(OpCodes.imul));
		}
		
		return result;
	}
	
	
	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return new TypeDescriptor("I");
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}
	
	

}
