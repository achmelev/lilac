package org.jasm.parser;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	private SymbolTable parent = null;
	private Map<String, ISymbolTableEntry> symbols = new HashMap<>();
	
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}
	
	public void add(ISymbolTableEntry entry) {
		if (symbols.containsKey(entry.getSymbolName())) {
			throw new IllegalStateException("There is already a symbol with name "+entry.getSymbolName());
		}
		
		symbols.put(entry.getSymbolName(), entry);
	}
	
	public boolean contains(String name) {
		return symbols.containsKey(name);
	}
	
	public boolean containsWithRecursion(String name) {
		boolean result =  symbols.containsKey(name);
		if (!result && parent != null) {
			result = parent.containsWithRecursion(name);
		}
		return result;
	}
	
	public ISymbolTableEntry get(String name) {
		return symbols.get(name);
	}
	
	public ISymbolTableEntry getWithRecursion(String name) {
		ISymbolTableEntry result =  symbols.get(name);
		if (result == null && parent != null) {
			result = parent.getWithRecursion(name);
		}
		return result;
	}
}
