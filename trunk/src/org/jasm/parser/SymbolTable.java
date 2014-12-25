package org.jasm.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	public void replace(ISymbolTableEntry old, ISymbolTableEntry new_) {
		Set<String> keys = new HashSet<String>();
		keys.addAll(symbols.keySet());
		for (String key:keys) {
			if (symbols.get(key) == old) {
				symbols.put(key, new_);
				break;
			}
		}
		
	}
	
	public boolean contains(String name) {
		if (symbols.containsKey(name)) {
			ISymbolTableEntry value = symbols.get(name);
			return !value.hasErrors();
		} else {
			return false;
		}
	}
	
	public boolean containsWithRecursion(String name) {
		boolean result =  symbols.containsKey(name);
		if (!result && parent != null) {
			result = parent.containsWithRecursion(name);
		}
		return result;
	}
	
	public ISymbolTableEntry get(String name) {
		if (!contains(name)) {
			return null;
		}
		return symbols.get(name);
	}
	
	public ISymbolTableEntry getWithRecursion(String name) {
		ISymbolTableEntry result =  get(name);
		if (result == null && parent != null) {
			result = parent.getWithRecursion(name);
		}
		return result;
	}
}
