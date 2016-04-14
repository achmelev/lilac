package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackmapSameLocalsStackOverflowException extends VerifyException {

	public StackmapSameLocalsStackOverflowException(int index, int maxStack, int actual) {
		super(index, "stackmap frame stack overflow "+actual+">"+maxStack);
	}

}
