package org.jasm.item.instructions.verify.error;

import org.jasm.item.instructions.verify.VerifyException;

public class StackmapAppendOverflowException extends VerifyException {

	public StackmapAppendOverflowException(int index, int appendSize, int activeLocals, int maxLocals) {
		super(index, "stackmap frame locals overflow "+activeLocals+"+"+appendSize+">"+maxLocals);

	}

}
