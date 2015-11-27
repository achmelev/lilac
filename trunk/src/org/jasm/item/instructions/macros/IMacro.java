package org.jasm.item.instructions.macros;

import java.util.List;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.type.descriptor.TypeDescriptor;

public interface IMacro {
	
	public void init(MacroCall call, Instructions instrs);
	public boolean resolve();
	public List<AbstractInstruction> createInstructions();
	public boolean hasReturnValue();
	public TypeDescriptor getReturnType();

}
