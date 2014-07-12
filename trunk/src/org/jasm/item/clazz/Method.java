package org.jasm.item.clazz;

import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.MethodModifier;

public class Method extends AbstractClassMember<MethodModifier> {
	
	public Method() {
		
	}
	
	public Method(Utf8Info name, Utf8Info descriptor, MethodModifier modifier) {
		super(name, descriptor,modifier);
	}

	@Override
	public String getPrintName() {
		return "method";
	}

	@Override
	protected MethodModifier createModifier(int value) {
		return new MethodModifier(value);
	}

}
