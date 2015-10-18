package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class LocalVariablesMissingException extends VerifyException {
	
	public LocalVariablesMissingException() {
		super(-1, "size of the local variable array is too little");
	}

}
