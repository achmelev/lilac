package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class InconsistentStackValueException extends VerifyException {

	public InconsistentStackValueException(int index, int stackPos, VerificationType expected, VerificationType actual) {
		super(index, "inconsistent values at stack position "+stackPos+" "+expected+"!="+actual);
		// TODO Auto-generated constructor stub
	}

}
