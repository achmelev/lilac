package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackmapFullStackOverflowException extends VerifyException {

	public StackmapFullStackOverflowException(int index, int maxStack, int actual) {
		super(index, "stackmap frame stack overflow "+actual+">"+maxStack);
	}

}
