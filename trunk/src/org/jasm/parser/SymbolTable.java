package org.jasm.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
	private Map<String, ISymbolTableEntry> symbols = new HashMap<>();
	private Map<ISymbolTableEntry, List<String>> entries = new HashMap<>();
	
	
	public void add(ISymbolTableEntry entry) {
		if (symbols.containsKey(entry.getSymbolName())) {
			throw new IllegalStateException("There is already a symbol with name "+entry.getSymbolName());
		}
		
		symbols.put(entry.getSymbolName(), entry);
		List<String> names = entries.get(entry);
		if (names == null) {
			names = new ArrayList<String>();
			entries.put(entry, names);
		}
		names.add(entry.getSymbolName());
	
	}
	
	public void replace(ISymbolTableEntry old, ISymbolTableEntry new_) {
		if (!contains(old)) {
			return;
		}
		List<String> keys = entries.remove(old);
		for (String key:keys) {
			symbols.put(key, new_);
		}
		entries.put(new_, keys);
		
	}
	
	public boolean contains(String name) {
		if (symbols.containsKey(name)) {
			ISymbolTableEntry value = symbols.get(name);
			return !value.hasErrors();
		} else {
			return false;
		}
	}
	
	public boolean contains(ISymbolTableEntry entry) {
		return entries.containsKey(entry);
	}
	
	
	public ISymbolTableEntry get(String name) {
		if (!contains(name)) {
			return null;
		}
		return symbols.get(name);
	}
	
	public List<String> getNames(ISymbolTableEntry entry) {
		if (!contains(entry)) {
			return new ArrayList<String>();
		}
		return entries.get(entry);
	}
	
}
