package org.jasm.item.instructions.verify.error;

public class MissingStackmapsDeclaration extends AbstractTypeCheckingException {

	public MissingStackmapsDeclaration() {
		super(-1, "no stackmap declaration found");
	}

}
