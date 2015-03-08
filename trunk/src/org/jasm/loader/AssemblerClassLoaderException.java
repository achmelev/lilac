package org.jasm.loader;


public class AssemblerClassLoaderException extends RuntimeException {
	
	
	private String resourcePath;
	
	public AssemblerClassLoaderException(String message, String resourcePath) {
		super(message);
		this.resourcePath = resourcePath;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	
	
	

}
