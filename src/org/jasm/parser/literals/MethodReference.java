package org.jasm.parser.literals;


import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class MethodReference extends AbstractLiteral implements IMacroArgument {
	
	private boolean isValid = false;
	private String className = null;
	private String methodName = null;
	private MethodDescriptor descriptor = null;

	public MethodReference(int line, int charPosition, String content) {
		super(line, charPosition, content);
		parse();
	}
	
	private void parse() {
		String content = getContent();
		String prefix = getContent().substring(0, content.indexOf('@'));
		String postfix = getContent().substring(content.indexOf('@')+1, content.length());
		if (prefix.indexOf('.')<0) {
			isValid = false;
		} else {
			methodName = prefix.substring(prefix.lastIndexOf('.')+1, prefix.length());
			className = prefix.substring(0, prefix.lastIndexOf('.'));
			descriptor = new MethodDescriptor(postfix);
			isValid = true;
		}
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public MethodDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public String getInvalidErrorMessage() {
		return "malformed method reference";
	}
	
	

}
