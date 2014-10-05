package org.jasm.item.clazz;

import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.item.modifier.FieldModifier;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

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
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	protected FieldModifier createModifier(int value) {
		return new FieldModifier(value);
	}

	@Override
	protected void verifyDescriptor(SymbolReference ref, String descriptor) {
		try {
			TypeDescriptor d = new TypeDescriptor(descriptor);
		} catch (IllegalDescriptorException e) {
			emitError(ref, "malformed field descriptor "+descriptor);
		}
		
	}

	@Override
	protected void verifyName(SymbolReference ref, String name) {
		if (!IdentifierUtils.isValidIdentifier(name)) {
			emitError(ref, "invalid field name: "+name);
		}
		
	}

}
