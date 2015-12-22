package org.jasm.test.macro;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class TestPushMacro extends AbstractMacro {
	
	private TypeDescriptor returnType = null;
	private boolean toBox = false;
	private String boxType = null;

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		IMacroArgument arg = getArgument(0);
		if (!toBox) {
			pushArgument(arg, result);
		} else {//primitive
			createInstructions(arg, returnType, result);
		}
		
		return result;
	}
	
	private List<AbstractInstruction> createInstructions(IMacroArgument arg, TypeDescriptor desc, List<AbstractInstruction> result) {
		
		String clazz = returnType.getClassName();
		result.add(createConstantPoolInstruction(OpCodes.new_, getClassInfo(clazz)));
		result.add(createArgumentLessInstruction(OpCodes.dup));
		pushArgument(arg, result);
		result.add(createConstantPoolInstruction(OpCodes.invokespecial, getMethodRefInfo(clazz, "<init>", 
				new MethodDescriptor("("+boxType+")V"))));
		
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
		if (getNumberOfArguments() != 1) {
			emitError(null, "wrong number of arguments");
		} else {
			IMacroArgument arg = getArgument(0);
			TypeDescriptor type = getArgumentType(arg);
			if (type == null) {
				emitError(null, "wrong argument type");
			} else {
				if (!type.isPrimitive()) {
					returnType = type;
				} else {
					toBox = true;
					TypeDescriptor desc = type;
					boxType = desc.getValue();
					String clazz = null;
					if (desc.isBoolean()) {
						clazz = "java/lang/Boolean";
					} else if (desc.isByte()) {
						clazz = "java/lang/Byte";
					} else if (desc.isCharacter()) {
						clazz = "java/lang/Character";
					} else if (desc.isDouble()) {
						clazz = "java/lang/Double";
					} else if (desc.isFloat()) {
						clazz = "java/lang/Float";
					} else if (desc.isInteger()) {
						clazz = "java/lang/Integer";
					} else if (desc.isLong()) {
						clazz = "java/lang/Long";
					} else if (desc.isShort()) {
						clazz = "java/lang/Short";
					} else {
						throw new IllegalArgumentException(desc.getValue());
					}
					returnType = new TypeDescriptor("L"+clazz+";");
				}
				
				result = true;
			}
		}
		
		return result;
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}

}
