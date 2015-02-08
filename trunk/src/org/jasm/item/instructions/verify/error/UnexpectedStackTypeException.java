package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class UnexpectedStackTypeException extends VerifyException {

	public UnexpectedStackTypeException(int index, VerificationType expected, VerificationType actual) {
		super(index, "unexpected value type on the stack "+expected.toString()+"!="+actual.toString());
	}

}
