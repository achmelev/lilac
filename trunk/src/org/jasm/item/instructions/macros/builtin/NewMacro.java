package org.jasm.item.instructions.macros.builtin;

import java.util.ArrayList;
import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.MultianewarrayInstruction;
import org.jasm.item.instructions.NewarrayInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class NewMacro extends AbstractMacro {
	
	private ClassInfo clazz = null;
	private MethodrefInfo constructor;

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		if (!clazz.isArray()) {
			MethodDescriptor md = new MethodDescriptor(constructor.getDescriptor());
			result.add(createConstantPoolInstruction(OpCodes.new_, clazz));
			result.add(createArgumentLessInstruction(OpCodes.dup));
			for (int i=2; i<getNumberOfArguments(); i++) {
				TypeDescriptor t1 = getArgumentType(getArgument(i));
				TypeDescriptor t2 = md.getParameters().get(i-2);
				pushArgument(getArgument(i), result);
				cast(t1, t2, result);
			}
			result.add(createConstantPoolInstruction(OpCodes.invokespecial, constructor));
			
		} else {
			TypeDescriptor arrayType = clazz.getDescriptor();
			if (arrayType.getArrayDimension() == 1) {
				pushArgument(getArgument(1), result);
				cast(getArgumentType(getArgument(1)), new TypeDescriptor("I"), result);
				if (arrayType.getComponentType().isPrimitive()) {
					short type = -1;
					TypeDescriptor componentType = arrayType.getComponentType();
					if (componentType.isBoolean()) {
						type = JasmConsts.ARRAY_TYPE_BOOLEAN;
					} else if (componentType.isByte()) {
						type = JasmConsts.ARRAY_TYPE_BYTE;
					} else if (componentType.isCharacter()) {
						type = JasmConsts.ARRAY_TYPE_CHAR;
					} else if (componentType.isDouble()) {
						type = JasmConsts.ARRAY_TYPE_DOUBLE;
					} else if (componentType.isFloat()) {
						type = JasmConsts.ARRAY_TYPE_FLOAT;
					} else if (componentType.isInteger()) {
						type = JasmConsts.ARRAY_TYPE_INT;
					} else if (componentType.isLong()) {
						type = JasmConsts.ARRAY_TYPE_LONG;
					} else if (componentType.isShort()) {
						type = JasmConsts.ARRAY_TYPE_SHORT;
					} else {
						throw new IllegalStateException(arrayType.getValue());
					}
					result.add(new NewarrayInstruction(type));
				} else {
					ClassInfo componentClass = getClassInfo(arrayType.getComponentType().getClassName());
					result.add(createConstantPoolInstruction(OpCodes.anewarray, componentClass));
				}
			} else {
				for (int i=1; i<getNumberOfArguments(); i++) {
					pushArgument(getArgument(i), result);
					cast(getArgumentType(getArgument(i)), new TypeDescriptor("I"), result);
				}
				MultianewarrayInstruction instr = new MultianewarrayInstruction((short)arrayType.getArrayDimension(), clazz);
				instr.setResolved(true);
				result.add(instr);
			}
		}
		
		return result;
	}

	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		if (clazz.isArray()) {
			return new TypeDescriptor(clazz.getClassName());
		} else {
			return new TypeDescriptor("L"+clazz.getClassName()+";");
		}
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return (index == 1 && (isLocalMethodSymbolReference(arg) || (isConstantSymbolReference(arg) && getConstantSymbolReferenceValue(arg) instanceof MethodrefInfo)));
	}

	@Override
	protected boolean doResolve() {
		if (getNumberOfArguments() == 0) {
			emitError(null, "wrong number of arguments");
		} else {
			IMacroArgument firstArg = getArgument(0);
			if (isConstantSymbolReference(firstArg) && getConstantSymbolReferenceValue(firstArg) instanceof ClassInfo) {
				clazz = (ClassInfo)getConstantSymbolReferenceValue(firstArg);
				if (!clazz.isArray()) {
					if (getNumberOfArguments() == 1) {
						emitError(null, "wrong number of arguments");
					} else {
						IMacroArgument secondArg = getArgument(1);
						if (isLocalMethodSymbolReference(secondArg) || (isConstantSymbolReference(secondArg) && getConstantSymbolReferenceValue(secondArg) instanceof MethodrefInfo)) {
							if (isLocalMethodSymbolReference(secondArg)) {
								Method m = getLocalMethodSymbolRefferenceValue(secondArg);
								constructor = (MethodrefInfo)getMethodRefInfo(getThisClass().getClassName(), m.getName().getValue(), m.getMethodDescriptor());
							} else {
								constructor = (MethodrefInfo)getConstantSymbolReferenceValue(secondArg);
							}
							
							if (constructor.getClassName().equals(clazz.getClassName()) && constructor.getName().equals("<init>") && new MethodDescriptor(constructor.getDescriptor()).isVoid()) {
								MethodDescriptor md = new MethodDescriptor(constructor.getDescriptor());
								if (getNumberOfArguments() == md.getParameters().size()+2) {
									for (int i=2; i<getNumberOfArguments(); i++) {
										TypeDescriptor t1 = getArgumentType(getArgument(i));
										TypeDescriptor t2 = md.getParameters().get(i-2);
										if (!canCast(t1, t2)) {
											emitError(getArgument(i).getSourceLocation(), "can not cast "+t1.getValue()+" to "+t2.getValue());
										}
									}
								} else {
									emitError(null, "wrong number of arguments");
								}
							} else {
								emitError(secondArg.getSourceLocation(), "wrong method type");
							}
							
						} else {
							emitError(secondArg.getSourceLocation(), "wrong argument type");
						}
					}
				} else {
					TypeDescriptor arrayType = clazz.getDescriptor();
					if (getNumberOfArguments() == arrayType.getArrayDimension()+1) {
						for (int i=1; i<getNumberOfArguments(); i++) {
							TypeDescriptor t1 = getArgumentType(getArgument(i));
							TypeDescriptor t2 = new TypeDescriptor("I");
							if (t1 == null)  {
								emitError(getArgument(i).getSourceLocation(), "wrong argument type");
							} else if (!canCast(t1, t2)) {
								emitError(getArgument(1).getSourceLocation(), "can not cast "+t1.getValue()+" to "+t2.getValue());
							} 
						}
					} else {
						emitError(null, "wrong number of arguments");
					}
				}
			} else {
				emitError(firstArg.getSourceLocation(), "wrong argument type");
			}
		}
		
		return !hasError();
	}

}
