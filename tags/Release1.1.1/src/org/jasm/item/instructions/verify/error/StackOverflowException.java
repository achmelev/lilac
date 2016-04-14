package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackOverflowException extends VerifyException {

	public StackOverflowException(int index) {
		super(index, "stack overflow");
	}

}
