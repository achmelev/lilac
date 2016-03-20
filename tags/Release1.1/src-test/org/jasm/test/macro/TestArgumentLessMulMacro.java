package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestArgumentLessMulMacro extends AbstractMacro {

	@Override
	public boolean doResolve() {
		return true;
	}

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		result.add(createSipushInstruction((short)10));
		result.add(createSipushInstruction((short)15));
		result.add(new ArgumentLessInstruction(OpCodes.imul));
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
