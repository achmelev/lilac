package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class UnexpectedStackTypeException extends VerifyException {

	public UnexpectedStackTypeException(int index, int stackPos,VerificationType expected, VerificationType actual) {
		super(index, "unexpected value type at the stack position "+stackPos+" "+expected.toString()+"!="+actual.toString());
	}

}
