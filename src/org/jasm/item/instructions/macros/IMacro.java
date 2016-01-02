package org.jasm.item.instructions.macros;

import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.type.descriptor.TypeDescriptor;

public interface IMacro {
	
	public void init(MacroCall call, Instructions instrs);
	public void init_generated(List<IMacroArgument> arguments, IMacro parent);
	public boolean resolve();
	public List<AbstractInstruction> createInstructions();
	public boolean hasReturnValue();
	public TypeDescriptor getReturnType();
	public MacroCall getCall();
	public Instructions getInstructions();
	public IMacro getParentMacro();

}
