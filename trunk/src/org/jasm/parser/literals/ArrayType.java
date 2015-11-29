package org.jasm.parser.literals;

import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.TypeDescriptor;

public class ArrayType extends AbstractLiteral implements IMacroArgument {
	
	private TypeDescriptor descriptor;

	public ArrayType(int line, int charPosition, String content) {
		super(line, charPosition, content);
		this.descriptor = new TypeDescriptor(content);
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getInvalidErrorMessage() {
		return null;
	}

	public TypeDescriptor getDescriptor() {
		return descriptor;
	}

}
