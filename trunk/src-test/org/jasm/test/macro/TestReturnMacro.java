package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestReturnMacro extends AbstractMacro {

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		pushArgument(getArgument(0), result);
		TypeDescriptor desc = getArgumentType(getArgument(0));
		if (desc.isBoolean() || desc.isByte() || 
				desc.isCharacter() || desc.isInteger() || desc.isShort()) {
			result.add(createArgumentLessInstruction(OpCodes.ireturn));
		} else if (desc.isDouble()) {
			result.add(createArgumentLessInstruction(OpCodes.dreturn));
		} else if (desc.isFloat()) {
			result.add(createArgumentLessInstruction(OpCodes.freturn));
		} else if (desc.isLong()) {
			result.add(createArgumentLessInstruction(OpCodes.lreturn));
		} else {
			result.add(createArgumentLessInstruction(OpCodes.areturn));
		}
		
		return result;
	}

	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return getArgumentType(getArgument(0));
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}

	@Override
	protected boolean doResolve() {
		if (getNumberOfArguments() !=1) {
			emitError(null, "wrong number of arguments");
			return false;
		}
		return true;
	}

}
