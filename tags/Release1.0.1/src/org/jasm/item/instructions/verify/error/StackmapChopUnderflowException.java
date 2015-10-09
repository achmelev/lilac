package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackmapChopUnderflowException extends VerifyException {

	public StackmapChopUnderflowException(int index, int chop, int activeLocals) {
		super(index, "stackmap frame locals underflow "+activeLocals+"<"+chop);

	}

}
