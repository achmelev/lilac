package org.jasm.item.clazz;

import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.FieldModifier;

public class Field extends AbstractClassMember<FieldModifier> {
	
	public Field() {
		
	}
	
	public Field(Utf8Info name, Utf8Info descriptor, FieldModifier modifier) {
		super(name, descriptor,modifier);
	}

	@Override
	public String getPrintName() {
		return "field";
	}

	@Override
	protected FieldModifier createModifier(int value) {
		return new FieldModifier(value);
	}

}
