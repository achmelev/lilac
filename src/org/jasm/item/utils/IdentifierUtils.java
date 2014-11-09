package org.jasm.item.utils;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.parser.literals.SymbolReference;

public class IdentifierUtils {
	
	public static boolean isValidIdentifier(String value) {
		if (value.length() == 0) {
			return false;
		}
		
		if (Character.isJavaIdentifierStart(value.charAt(0))) {
			for (int i=1; i<value.length(); i++) {
				if (Character.isJavaIdentifierPart(value.charAt(i))) {
					
				} else {
					return false;
				}
			}
			return true;
			
		} else {
			return false;
		}
		
	}
	
	public static boolean isValidJasmClassName(String name) {
		String[] ids = name.split("/");
		if (ids.length==0) {
			return false;
		} else {
			for (String s: ids) {
				if (!isValidIdentifier(s)) {
					return false;
				} 
			}
			
			return true;
		}
	}
	
	public static boolean isValidJavaClassName(String name) {
		String[] ids = name.split("\\.");
		if (ids.length==0) {
			return false;
		} else {
			for (String s: ids) {
				if (!isValidIdentifier(s)) {
					return false;
				} 
			}
			
			return true;
		}
	}
	
	public static String convertToJavaClassName(String name) {
		if (!isValidJasmClassName(name)) {
			throw new IllegalArgumentException("malformed jasm class name:"+name);
		}
		return name.replace('/', '.');
	}
	
	public static String convertToJasmClassName(String name) {
		if (!isValidJavaClassName(name)) {
			throw new IllegalArgumentException("malformed java class name:"+name);
		}
		return name.replace('.', '/');
	}
	
	public static boolean checkIdentifier(AbstractByteCodeItem source, SymbolReference ref, Utf8Info value) {
		if (!IdentifierUtils.isValidIdentifier(value.getValue())) {
			source.emitError(ref, "malformed identifier: "+value.getValue());
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean checkMethodName(AbstractByteCodeItem source, SymbolReference ref, Utf8Info value) {
		if (!IdentifierUtils.isValidIdentifier(value.getValue()) && !(value.getValue().equals("<init>") || value.getValue().equals("<clinit>"))) {
			source.emitError(ref, "invalid method name: "+value.getValue());
			return false;
		} else {
			return true;
		}
	}

}
