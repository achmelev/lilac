package org.jasm.item.instructions.verify.error;

public class MissingStackmapException extends AbstractTypeCheckingException {

	public MissingStackmapException(int index) {
		super(index, "missing stackmap for a branch or exception handler target");
	}

}
