package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackmapFullLocalsOverflowException extends VerifyException {

	public StackmapFullLocalsOverflowException(int index, int maxLocals, int actual) {
		super(index, "stackmap frame locals overflow "+actual+">"+maxLocals);

	}

}
