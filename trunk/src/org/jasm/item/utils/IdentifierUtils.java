package org.jasm.item.utils;

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

}
