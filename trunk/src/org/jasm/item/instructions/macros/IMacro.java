package org.jasm.item.instructions.macros;

import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.Instructions;

public interface IMacro {
	
	public void init(MacroCall call, Instructions instrs, List<IMacroArgument> arguments);
	public boolean resolve();
	public List<AbstractInstruction> createInstructions();

}
