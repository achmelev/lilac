package org.jasm.item;

import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;

public interface IErrorEmitter {

	public abstract void emitError(AbstractLiteral literal, String message);
	public abstract void emitErrorOnLocation(SourceLocation sl, String message);
	public abstract void emitInternalError(Throwable e);

}