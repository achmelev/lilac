package org.jasm.item.classpath;

public interface IClassPathEntry {
	
	public ExternalClassInfo findClass(String className);
	public boolean isInvalid();

}
