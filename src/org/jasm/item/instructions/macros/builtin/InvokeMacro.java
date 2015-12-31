package org.jasm.item.instructions.macros.builtin;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class InvokeMacro extends AbstractMacro {
	
	private short opCode;
	private ClassInfo clazz;
	private AbstractConstantPoolEntry method;
	private MethodDescriptor methodDescriptor;
	
	public InvokeMacro(short opCode) {
		this.opCode = opCode;
	}

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		int startParametersIndex = (OpCodes.invokestatic == opCode)?1:2;
		if (startParametersIndex == 2) {
			TypeDescriptor t1 = getArgumentType(getArgument(1));
			TypeDescriptor t2 = clazz.getDescriptor();
			pushArgument(getArgument(1), result);
			cast(t1, t2, result);
		}
		for (int i=startParametersIndex; i<getNumberOfArguments(); i++) {
			TypeDescriptor t1 = getArgumentType(getArgument(i));
			TypeDescriptor t2 = methodDescriptor.getParameters().get(i-startParametersIndex);
			pushArgument(getArgument(i), result);
			cast(t1, t2, result);
		}
		result.add(createConstantPoolInstruction(opCode, method));
		return result;
	}

	@Override
	public boolean hasReturnValue() {
		return !methodDescriptor.isVoid();
	}

	@Override
	public TypeDescriptor getReturnType() {
		return methodDescriptor.getReturnType();
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return (index == 0) && (isLocalMethodSymbolReference(arg) || (isConstantSymbolReference(arg) 
				&& (getConstantSymbolReferenceValue(arg) instanceof MethodrefInfo
				    || getConstantSymbolReferenceValue(arg) instanceof InterfaceMethodrefInfo)));
	}

	@Override
	protected boolean doResolve() {
		if (getNumberOfArguments()>=1) {
			IMacroArgument methodArg = getArgument(0);
			if (isLocalMethodSymbolReference(methodArg) || (isConstantSymbolReference(methodArg) 
					&& (getConstantSymbolReferenceValue(methodArg) instanceof MethodrefInfo
					    || getConstantSymbolReferenceValue(methodArg) instanceof InterfaceMethodrefInfo))) {
				if (isLocalMethodSymbolReference(methodArg)) {
					Method m = getLocalMethodSymbolRefferenceValue(methodArg);
					method = (MethodrefInfo)getMethodRefInfo(getThisClass().getClassName(), m.getName().getValue(), m.getMethodDescriptor());
				} else {
					method = getConstantSymbolReferenceValue(methodArg);
				}
				if (method instanceof InterfaceMethodrefInfo && opCode != OpCodes.invokeinterface) {
					emitError(methodArg.getSourceLocation(), "wrong method type");
				}
				if (!hasError()) {
					String descValue = (method instanceof MethodrefInfo)?((MethodrefInfo)method).getDescriptor():((InterfaceMethodrefInfo)method).getDescriptor();
					clazz = (method instanceof MethodrefInfo)?((MethodrefInfo)method).getClassReference():((InterfaceMethodrefInfo)method).getClassReference();
					methodDescriptor = new MethodDescriptor(descValue);
					int expectedNumberOfArguments = (opCode == OpCodes.invokestatic)?methodDescriptor.getParameters().size(): methodDescriptor.getParameters().size()+1;
					if (getNumberOfArguments() == expectedNumberOfArguments+1) {
						int startParametersIndex = (OpCodes.invokestatic == opCode)?1:2;
						if (startParametersIndex == 2) {
							TypeDescriptor t1 = getArgumentType(getArgument(1));
							TypeDescriptor t2 = clazz.getDescriptor();
							if (!canCast(t1, t2)) {
								emitError(getArgument(1).getSourceLocation(), "wrong argument type");
							}
						}
						for (int i=startParametersIndex; i<getNumberOfArguments(); i++) {
							TypeDescriptor t1 = getArgumentType(getArgument(i));
							TypeDescriptor t2 = methodDescriptor.getParameters().get(i-startParametersIndex);
							if (!canCast(t1, t2)) {
								emitError(getArgument(1).getSourceLocation(), "wrong argument type");
							}
						}
					} else {
						emitError(null, "wrong number of arguments");
					}
				}
				
			} else {
				emitError(methodArg.getSourceLocation(), "wrong argument type");
			}
		} else {
			emitError(null, "wrong number of arguments");
		}
		
		return !hasError();
	}

}
