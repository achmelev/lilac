package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class TypeInferencingException extends RuntimeException {
	
	private VerifyException cause;
	
	public TypeInferencingException(VerifyException e) {
		this.cause = e;
	}

	public VerifyException getCause() {
		return cause;
	}
	
	

}
