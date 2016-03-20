package org.jasm.item.instructions.macros.builtin;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class ConcatMacro extends AbstractMacro {

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		ClassInfo stringBufferClass = getClassInfo("java/lang/StringBuffer");
		MethodrefInfo stringBufferConstructor = getMethodRefInfo("java/lang/StringBuffer", "<init>", new MethodDescriptor("()V"));
		result.add(createConstantPoolInstruction(OpCodes.new_, stringBufferClass));
		result.add(createArgumentLessInstruction(OpCodes.dup));
		result.add(createConstantPoolInstruction(OpCodes.invokespecial, stringBufferConstructor));
		for (int i=0;i<getNumberOfArguments(); i++) {
			IMacroArgument arg = getArgument(i);
			TypeDescriptor type = getArgumentType(arg);
			String desc = null;
			if ((type.isPrimitive()) ||
					(type.isObject() && type.getClass().equals("java/lang/String"))
					
				) {
				desc = type.getValue();
				if (desc.equals("B") ||desc.equals("S")) {
					desc = "I";
				}
			} else {
				desc = "Ljava/lang/Object;";
			}
			MethodrefInfo appendMethod = getMethodRefInfo("java/lang/StringBuffer", "append", new MethodDescriptor("("+desc+")Ljava/lang/StringBuffer;"));
			pushArgument(arg, result);
			result.add(createConstantPoolInstruction(OpCodes.invokevirtual, appendMethod));
		}
		
		MethodrefInfo stringBufferToString = getMethodRefInfo("java/lang/StringBuffer", "toString", new MethodDescriptor("()Ljava/lang/String;"));
		result.add(createConstantPoolInstruction(OpCodes.invokevirtual, stringBufferToString));
		
		return result;
	}
	

	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return new TypeDescriptor("Ljava/lang/String;");
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}

	@Override
	protected boolean doResolve() {
		return true;
	}

}
