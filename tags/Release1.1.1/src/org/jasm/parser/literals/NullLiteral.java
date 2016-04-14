package org.jasm.parser.literals;

import org.jasm.item.instructions.macros.IMacroArgument;

public class NullLiteral extends AbstractLiteral implements IMacroArgument {

	public NullLiteral(int line, int charPosition, String content) {
		super(line, charPosition, content);
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getInvalidErrorMessage() {
		return null;
	}

}
