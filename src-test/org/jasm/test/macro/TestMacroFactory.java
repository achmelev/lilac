package org.jasm.test.macro;

import org.jasm.item.instructions.macros.IMacro;
import org.jasm.item.instructions.macros.IMacroFactory;

public class TestMacroFactory implements IMacroFactory {

	@Override
	public IMacro createMacroByName(String name) {
		if (name.equals("test.argumentlessmul")) {
			return new TestArgumentLessMulMacro();
		} else if (name.equals("test.imul")) {
			return new TestMulMacro();
		} else if (name.equals("test.push")) {
			return new TestPushMacro();
		} else if (name.equals("test.condpush")) {
			return new TestConditionalPushMacro();
		} else if (name.equals("test.switch")) {
			return new TestSwitchMacro();	
		} else {
			return null;
		}
	}

}
