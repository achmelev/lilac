package org.jasm.item.instructions.macros;

import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.JavaTypeLiteral;

public interface IMacroArgument {
	public SourceLocation getSourceLocation();
	public boolean isValid();
	public String getInvalidErrorMessage();
}
