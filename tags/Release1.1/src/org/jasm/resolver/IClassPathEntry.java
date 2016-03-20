package org.jasm.resolver;

public interface IClassPathEntry {
	
	public byte [] findBytes(String resourceName);
	public ExternalClassInfo findClass(String className);
	public boolean isInvalid();

}
