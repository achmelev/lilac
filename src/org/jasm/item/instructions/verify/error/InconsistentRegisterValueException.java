package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class InconsistentRegisterValueException extends VerifyException {

	public InconsistentRegisterValueException(int index, int register, VerificationType expected, VerificationType actual) {
		super(index, "inconsistent values at regsiter "+register+" "+expected+"!="+actual);
	}

}
