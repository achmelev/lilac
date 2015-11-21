package org.jasm.item.instructions.macros;

import java.util.List;

import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractMacro implements IMacro {
	
	protected List<IMacroArgument> arguments;
	protected Instructions instructions;
	protected MacroCall call;

	@Override
	public void init(MacroCall call, Instructions instrs, List<IMacroArgument> arguments) {
		instructions = instrs;
		this.arguments = arguments;
		this.call = call;
	}
	
	protected LocalVariable getVariable(SymbolReference ref, boolean emitError) {
		LocalVariable var = instructions.getVariablesPool().getByName(ref.getSymbolName());
		if (var == null && emitError) {
			emitError(ref.getSourceLocation(), "unknown variable name: "+ref.getSymbolName());
		}
		return var;
	}
	
	protected void emitError(SourceLocation location, String message) {
		instructions.emitErrorOnLocation((location != null)?location:call.getSourceLocation(), message);
	}

}
