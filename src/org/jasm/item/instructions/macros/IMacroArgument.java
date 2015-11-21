package org.jasm.item.instructions.macros;

import org.jasm.parser.SourceLocation;

public interface IMacroArgument {
	public SourceLocation getSourceLocation();
	public boolean isValid();
	public String getInvalidErrorMessage();
}
