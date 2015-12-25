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

public class GetFieldMacro extends AbstractMacro {
	
	private FieldrefInfo fieldref;
	private boolean isStatic = false;

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		if (getNumberOfArguments() == 2) {
			pushArgument(getArgument(0), result);
		}
		IMacroArgument arg = (getNumberOfArguments() == 1)?getArgument(0):getArgument(1);
		result.add(createConstantPoolInstruction(isStatic?OpCodes.getstatic:OpCodes.getfield, fieldref));
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
		return isLocalFieldSymbolReference(arg) ||
				(isConstantSymbolReference(arg) 
						&& getConstantSymbolReferenceValue(arg) instanceof FieldrefInfo);
	}

	@Override
	protected boolean doResolve() {
		if (getNumberOfArguments()<1 || getNumberOfArguments()>2) {
			emitError(null, "wrong number of arguments");
			return false;
		} else {
			
			if (getNumberOfArguments() == 2) {
				TypeDescriptor t = getArgumentType(getArgument(0));
				if (t != null && t.isObject()) {
					
				} else {
					emitError(getArgument(0).getSourceLocation(), "wrong argument type");
					return false;
				}
			}
			
			IMacroArgument arg = (getNumberOfArguments() == 1)?getArgument(0):getArgument(1);
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
			
			if (getNumberOfArguments() == 1) {
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
		}
		
		return true;

		
	}

}
