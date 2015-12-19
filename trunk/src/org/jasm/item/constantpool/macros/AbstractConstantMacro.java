package org.jasm.item.constantpool.macros;

import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.parser.literals.Label;


public abstract class AbstractConstantMacro {
	
	protected ConstantPool parent;
	
	private SourceLocation sourceLocation;
	
	protected Label label = null;
	
	public abstract void resolve();

	public void setParent(ConstantPool parent) {
		this.parent = parent;
	}

	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}
	
	public void setLabel(Label label) {
		this.label = label;
	}

	protected void emitError(AbstractLiteral literal, String message) {
		if (literal != null) {
			parent.emitError(literal, message);
		} else {
			parent.emitErrorOnLocation(sourceLocation, message);
		}
	}
	
	protected void registerConstant(String name, AbstractConstantPoolEntry entry) {
		SourceLocation location = this.sourceLocation;
		if (label != null) {
			location = label.getSourceLocation();
		}
		entry.setLabel(new Label(location.getLine(), location.getCharPosition(), name));
		if (!parent.getSymbolTable().contains(entry.getSymbolName())) {
			parent.getSymbolTable().add(entry);
		} else {
			emitError(null, "dublicate constant declaration "+entry.getSymbolName());
		}
	}
}
