package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class FallOffException extends VerifyException {

	public FallOffException(int index) {
		super(index, "execution falls of the code's end");
	}

}
