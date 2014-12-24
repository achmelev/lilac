package org.jasm.loader;

import java.util.List;

import org.jasm.parser.ErrorMessage;

public class AssemblerClassLoaderException extends RuntimeException {
	
	
	private String resourcePath;
	private List<ErrorMessage> errorMessages;
	
	public AssemblerClassLoaderException(String message, String resourcePath, List<ErrorMessage> parserErrorMessages) {
		super(message);
		this.resourcePath = resourcePath;
		this.errorMessages = parserErrorMessages;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}
	
	

}
