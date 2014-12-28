package org.jasm.resolver;

public interface IClassPathEntry {
	
	public ExternalClassInfo findClass(String className);
	public boolean isInvalid();

}
