package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class LinearMethodException extends RuntimeException {
	
	private VerifyException cause;
	
	public LinearMethodException(VerifyException e) {
		this.cause = e;
	}

	public VerifyException getCause() {
		return cause;
	}
	
	

}
