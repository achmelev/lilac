package org.jasm.disassembler;

import java.util.HashSet;
import java.util.Set;

import org.jasm.item.utils.IdentifierUtils;

public class ClassNameGenerator {
	
	public static String THISCLASS = "ThisClass";
	
	private String thisClassName = "";
	
	private Set<String> usedShortClassNames = new HashSet<String>();
	private Set<String> usedFullClassNames = new HashSet<String>();
	
	public String createDisassemblerClassName(String className) {
		className = className.trim();
		if (!IdentifierUtils.isValidJasmClassName(className)) {
			return null;
		}
		
		
		String fullClassName = className.replace('/', '.');
		String shortClassName = className.substring(className.lastIndexOf('/')+1, className.length());
		if (className.equals(thisClassName)) {
			shortClassName = THISCLASS;
		}
		
		if (!usedShortClassNames.contains(shortClassName) && !NameGenerator.isKeyword(shortClassName)) {
			usedShortClassNames.add(shortClassName);
			return shortClassName;
		} else if (!usedFullClassNames.contains(fullClassName)) {
			usedFullClassNames.add(fullClassName);
			return fullClassName;
		} else {
			String candidate = fullClassName;
			int counter = 0;
			String result = candidate+"$"+counter;
			while (fullClassName.contains(result)) {
				counter++;
				result = candidate+"$"+counter;
			}
			usedFullClassNames.add(fullClassName);
			return fullClassName;
			
		}
		
	}

	public void setThisClassName(String thisClassName) {
		this.thisClassName = thisClassName;
	}
	
	

}
