package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class BadCodeException extends VerifyException {

	public BadCodeException(int index) {
		super(index, "Bad code (jsr,jsr_w,ret) found");
	}

}
