package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestSwitchMacro extends AbstractMacro {
	
	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		IMacroArgument condition = getArgument(0);
		IMacroArgument target = getArgument(1);
		pushArgument(condition, result);
		result.add(createBranchInstruction(OpCodes.ifne, (SymbolReference)target));
		
		return result;
	}
	
	
	@Override
	public boolean hasReturnValue() {
		return false;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return null;
	}

	@Override
	protected boolean doResolve() {
		
		boolean result = false;
		if (getNumberOfArguments() !=2) {
			emitError(null, "wrong number of arguments");
		} else {
			IMacroArgument condition = getArgument(0);
			IMacroArgument target = getArgument(1);
			if (getArgumentType(condition)!=null && getArgumentType(condition).isInteger()) {
				if (isInstructionSymbolReference(target)) {
					result = true;
				} else {
					emitError(target.getSourceLocation(), "wrong argument type");
				}
			} else {
				emitError(condition.getSourceLocation(), "wrong argument type");
			}
			
		}
		
		
		return result;
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return (index == 1 && isInstructionSymbolReference(arg));
	}

}
