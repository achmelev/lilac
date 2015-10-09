package org.jasm.parser.literals;

public class SymbolReference extends AbstractLiteral {
	
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
