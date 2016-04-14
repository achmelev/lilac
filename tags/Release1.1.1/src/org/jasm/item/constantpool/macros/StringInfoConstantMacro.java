package org.jasm.item.constantpool.macros;

import org.jasm.item.constantpool.StringInfo;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;

public class StringInfoConstantMacro extends AbstractConstantMacro {
	
	private SymbolReference name;
	private StringLiteral value;

	@Override
	public void resolve() {
		StringInfo str = parent.getOrAddStringInfo(value.getStringValue());
		registerConstant(name.getSymbolName(), str, label);
	}

	public void setName(SymbolReference name) {
		this.name = name;
	}

	public void setValue(StringLiteral value) {
		this.value = value;
	}
	
	

}
