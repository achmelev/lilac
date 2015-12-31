package org.jasm.item.instructions.macros.builtin;

import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.IMacro;
import org.jasm.item.instructions.macros.IMacroFactory;

public class BuiltinMacroFactory implements IMacroFactory {

	@Override
	public IMacro createMacroByName(String name) {
		if (name.equals(".concat")) {
			return new ConcatMacro();
		} else if (name.equals(".getfield")) {
			return new GetFieldMacro();
		} else if (name.equals(".putfield")) {
			return new PutFieldMacro();
		} else if (name.equals(".new")) {
			return new NewMacro();	
		} else if (name.equals(".invokevirtual")) {
			return new InvokeMacro(OpCodes.invokevirtual);
		} else if (name.equals(".invokeinterface")) {
			return new InvokeMacro(OpCodes.invokeinterface);	
		} else if (name.equals(".invokeinterface")) {
			return new InvokeMacro(OpCodes.invokeinterface);
		} else if (name.equals(".invokespecial")) {
			return new InvokeMacro(OpCodes.invokespecial);
		} else if (name.equals(".invokestatic")) {
			return new InvokeMacro(OpCodes.invokestatic);	
		} else {
			return null;
		}
	}

}
