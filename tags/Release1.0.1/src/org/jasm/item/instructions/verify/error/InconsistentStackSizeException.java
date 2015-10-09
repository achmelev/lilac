package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class InconsistentStackSizeException extends VerifyException {

	public InconsistentStackSizeException(int index, int expected, int actual) {
		super(index, "inconsistent stack sizes "+expected+"!="+actual);
	}

}
