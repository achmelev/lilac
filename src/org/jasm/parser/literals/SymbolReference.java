package org.jasm.parser.literals;

import org.jasm.item.instructions.macros.IMacroArgument;

public class SymbolReference extends AbstractLiteral implements IMacroArgument {
	
	private String referenceLabel;

	public SymbolReference(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getSymbolName() {
		return getContent();
	}

	public String getReferenceLabel() {
		return referenceLabel;
	}

	public void setReferenceLabel(String referenceLabel) {
		this.referenceLabel = referenceLabel;
	}
	
	

}
