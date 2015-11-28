package org.jasm.parser.literals;

import org.jasm.item.instructions.macros.IMacroArgument;

public class ClassReference extends AbstractLiteral implements IMacroArgument {
	
	private String className;

	public ClassReference(int line, int charPosition, String content) {
		super(line, charPosition, content);
		className = content;
		if (className.startsWith("/")) {
			this.className = this.className.substring(1, className.length());
		}
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getInvalidErrorMessage() {
		return null;
	}

	public String getClassName() {
		return className;
	}
	
	

}
