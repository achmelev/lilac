package org.jasm.item.constantpool.macros;

import org.jasm.parser.literals.ClassReference;

public class ClassInfoConstantMacro extends AbstractConstantMacro {
	
	private ClassReference name = null;
	
	public ClassInfoConstantMacro(ClassReference name) {
		this.name = name;
	}

	@Override
	public void resolve() {
		registerClassConstant(name.getClassName(), (label==null)?null:label.getLabel());
	}

}
