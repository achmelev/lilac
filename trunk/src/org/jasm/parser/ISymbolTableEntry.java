package org.jasm.parser;

public interface ISymbolTableEntry {
	
	public String getSymbolName();
	public String getSymbolTypeLabel();
	public SourceLocation getSourceLocation();
	public boolean hasResolveErrors();

}
