package org.jasm.item.constantpool.macros;

import org.jasm.parser.literals.JavaTypeLiteral;

public class ClassArrayInfoConstantMacro extends AbstractConstantMacro {
	
	private JavaTypeLiteral arrayType = null;
	
	public ClassArrayInfoConstantMacro(JavaTypeLiteral arrayType) {
		this.arrayType = arrayType;
	}

	@Override
	public void resolve() {
		if (arrayType.getDescriptor() != null) {
			registerClassConstant(arrayType.getDescriptor().getValue(), label.getLabel());
		}	
	}

}
