package org.jasm.parser.literals;

public class SymbolReference extends AbstractLiteral {

	public SymbolReference(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}
	
	public String getSymbolName() {
		return getContent();
	}

}
