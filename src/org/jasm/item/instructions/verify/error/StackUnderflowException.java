package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackUnderflowException extends VerifyException {

	public StackUnderflowException(int index) {
		super(index, "stack overflow");
	}

}
