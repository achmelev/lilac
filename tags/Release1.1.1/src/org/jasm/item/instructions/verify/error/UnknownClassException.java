package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class UnknownClassException extends VerifyException {

	public UnknownClassException(int index, String className) {
		super(index, "unknown class "+className);
	}

}
