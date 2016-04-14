package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class UnexpectedRegisterTypeException extends VerifyException {

	public UnexpectedRegisterTypeException(int index,int register, VerificationType expected, VerificationType actual) {
		super(index, "unexpected value type in the register "+register+":"+expected.toString()+"!="+actual.toString());
	}

}
