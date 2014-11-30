package org.jasm.parser;

public interface ISymbolTableEntry {
	
	public String getSymbolName();
	public SourceLocation getSourceLocation();
	public boolean hasResolveErrors();

}
