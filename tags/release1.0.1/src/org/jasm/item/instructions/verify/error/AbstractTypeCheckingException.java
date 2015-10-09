package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class AbstractTypeCheckingException extends VerifyException {

	public AbstractTypeCheckingException(int index, String message) {
		super(index, message);
	}

}
