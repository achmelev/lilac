package org.jasm.item.instructions.macros.builtin;

import java.util.ArrayList;
import java.util.List;

import jdk.internal.dynalink.beans.StaticClass;

import org.jasm.item.clazz.Field;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.TypeDescriptor;

public class PutFieldMacro extends AbstractMacro {
	
	private FieldrefInfo fieldref;
	private boolean isStatic = false;

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		if (getNumberOfArguments() == 3) {
			TypeDescriptor t = getArgumentType(getArgument(0));
			pushArgument(getArgument(0), result);
			cast(t, new TypeDescriptor("L"+fieldref.getClassName()+";"), result);
		}
		IMacroArgument valuearg = (getNumberOfArguments() == 2)?getArgument(1):getArgument(2);
		pushArgument(valuearg, result);
		cast(getArgumentType(valuearg), fieldref.getNameAndTypeReference().getTypeDescriptor(), result);
		result.add(createConstantPoolInstruction(isStatic?OpCodes.putstatic:OpCodes.putfield, fieldref));
		return result;
	}

	@Override
	public boolean hasReturnValue() {
		return true;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return new TypeDescriptor(fieldref.getDescriptor());
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return (isLocalFieldSymbolReference(arg) ||
				(isConstantSymbolReference(arg) 
						&& getConstantSymbolReferenceValue(arg) instanceof FieldrefInfo)) && (index == 0||index==1);
	}

	@Override
	protected boolean doResolve() {
		if (getNumberOfArguments()<2 || getNumberOfArguments()>3) {
			emitError(null, "wrong number of arguments");
			return false;
		} else {
			
			if (getNumberOfArguments() == 3) {
				TypeDescriptor t = getArgumentType(getArgument(0));
				if (t != null && t.isObject()) {
					
				} else {
					emitError(getArgument(0).getSourceLocation(), "wrong argument type");
					return false;
				}
			}
			
			IMacroArgument arg = (getNumberOfArguments() == 2)?getArgument(0):getArgument(1);
			Field localField = null;
			if (isLocalFieldSymbolReference(arg)) {
				localField = getLocalFieldSymbolRefferenceValue(arg);
				fieldref = getFieldRefInfo(getThisClass().getClassName(), localField.getName().getValue(), new TypeDescriptor(localField.getDescriptor().getValue()));
			} else  if (isConstantSymbolReference(arg) 
					&& getConstantSymbolReferenceValue(arg) instanceof FieldrefInfo) {
				fieldref = (FieldrefInfo)getConstantSymbolReferenceValue(arg);
			} else {
				emitError(arg.getSourceLocation(), "wrong argument type");
				return false;
			}
			
			if (getNumberOfArguments() == 2) {
				if (localField !=null && !localField.getModifier().isStatic()) {
					emitError(null, "wrong number of arguments");
					return false;
				} else {
					isStatic = true;
				}
			} else {
				if (localField !=null && localField.getModifier().isStatic()) {
					emitError(null, "wrong number of arguments");
					return false;
				} else {
					isStatic = false;
				}
			}
			
			IMacroArgument valuearg = (getNumberOfArguments() == 2)?getArgument(1):getArgument(2);
			if (!canCast(getArgumentType(valuearg), fieldref.getNameAndTypeReference().getTypeDescriptor())) {
				emitError(valuearg.getSourceLocation(), "wrong argument type");
				return false;
			}
			
		}
		
		return true;

		
	}

}
