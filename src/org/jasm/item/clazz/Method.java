package org.jasm.item.clazz;

import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.MethodDescriptor;
import org.jasm.item.modifier.MethodModifier;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

public class Method extends AbstractClassMember<MethodModifier> {
	
	private MethodDescriptor methodDescriptor =null;
	
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
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	protected MethodModifier createModifier(int value) {
		return new MethodModifier(value);
	}
	
	@Override
	protected void verifyDescriptor(SymbolReference ref, String descriptor) {
		try {
			MethodDescriptor d = new MethodDescriptor(descriptor);
			methodDescriptor = d;
		} catch (IllegalDescriptorException e) {
			emitError(ref, "malformed method descriptor "+descriptor);
		}
		
	}
	
	@Override
	protected void verifyName(SymbolReference ref, String name) {
		if (!IdentifierUtils.isValidIdentifier(name) && !(name.equals("<init>") || name.equals("<clinit>"))) {
			emitError(ref, "invalid method name: "+name);
		}
		
	}

	public MethodDescriptor getMethodDescriptor() {
		return methodDescriptor;
	}
	
	

}
