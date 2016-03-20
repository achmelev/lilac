package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestConditionalPushMacro extends AbstractMacro {
	
	private TypeDescriptor returnType = null;


	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		IMacroArgument condition = getArgument(0);
		IMacroArgument trueValue = getArgument(1);
		IMacroArgument falseValue = getArgument(2);
		pushArgument(condition, result);
		int falseIndex = result.size();
		pushArgument(falseValue, result);
		int trueIndex = result.size();
		pushArgument(trueValue, result);
		
		//insert branches
		result.add(falseIndex, createBranchInstruction(OpCodes.ifne, result.get(trueIndex)));
		falseIndex++;
		trueIndex++;
		result.add(trueIndex, createBranchInstruction(OpCodes.goto_, result.get(result.size()-1), true));
		
		return result;
	}
	
	
	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return returnType;
	}

	@Override
	protected boolean doResolve() {
		
		boolean result = false;
		if (getNumberOfArguments() !=3) {
			emitError(null, "wrong number of arguments");
		} else {
			IMacroArgument condition = getArgument(0);
			IMacroArgument trueValue = getArgument(1);
			IMacroArgument falseValue = getArgument(2);
			if (getArgumentType(condition)!=null && getArgumentType(condition).isInteger()) {
				if (getArgumentType(trueValue) != null) {
					if (getArgumentType(falseValue) != null) {
						if (getArgumentType(trueValue).equals(getArgumentType(falseValue))) {
							returnType = getArgumentType(trueValue);
							result = true;
						} else {
							emitError(null, "second and third arguments must have the same type");
						}
					} else {
						emitError(falseValue.getSourceLocation(), "wrong argument type");
					}
				} else {
					emitError(trueValue.getSourceLocation(), "wrong argument type");
				}
			} else {
				emitError(condition.getSourceLocation(), "wrong argument type");
			}
			
		}
		
		
		return result;
	}


	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}

}
