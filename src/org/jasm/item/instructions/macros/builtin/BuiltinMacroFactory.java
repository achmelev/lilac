package org.jasm.item.instructions.macros.builtin;

import org.jasm.item.instructions.macros.IMacro;
import org.jasm.item.instructions.macros.IMacroFactory;

public class BuiltinMacroFactory implements IMacroFactory {

	@Override
	public IMacro createMacroByName(String name) {
		if (name.equals("concat")) {
			return new ConcatMacro();
		} else {
			return null;
		}
	}

}
