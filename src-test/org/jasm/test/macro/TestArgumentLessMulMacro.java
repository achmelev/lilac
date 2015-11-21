package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.item.instructions.macros.AbstractMacro;

public class TestArgumentLessMulMacro extends AbstractMacro {

	@Override
	public boolean resolve() {
		return true;
	}

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		result.add(new SipushInstruction((short)10));
		result.add(new SipushInstruction((short)15));
		result.add(new ArgumentLessInstruction(OpCodes.imul));
		return result;
	}

}