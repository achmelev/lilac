package org.jasm.item.clazz;

import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.FieldModifier;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.TypeDescriptor;

public class Field extends AbstractClassMember<FieldModifier> {
	
	private TypeDescriptor typeDescriptor;
	private JavaTypeLiteral javaType;
	
	public Field() {
		
	}
	

	@Override
	public String getPrintName() {
		if (!modifier.hasNoFlags()) {
			return modifier.toString()+" field";
		} else {
			return "field";
		}
		
	}
	

	@Override
	protected FieldModifier createModifier(int value) {
		return new FieldModifier(value);
	}

	@Override
	protected void verifyDescriptor(SymbolReference ref, String descriptor) {
		try {
			typeDescriptor = new TypeDescriptor(descriptor);
		} catch (IllegalDescriptorException e) {
			emitError(ref, "malformed field descriptor "+descriptor);
		}
		
	}

	@Override
	protected void verifyName(SymbolReference ref, Utf8Info name) {
		IdentifierUtils.checkSimpleIdentifier(this, ref, name);
	}
	

	@Override
	protected void checkModifiers() {
		boolean valid = true;
		boolean isInInterface = getRoot().getModifier().isInterface();
		if (!isInInterface) {
			valid = checkPublicPrivateProtected();
			if (getModifier().isFinal() && getModifier().isVolatile()) {
				valid = false;
			}
		} else  {
			if (!getModifier().isPublic() || !getModifier().isStatic() || !getModifier().isFinal()) {
				valid = false;
			}
			if (getModifier().isEnum() || 
					getModifier().isPrivate() || 
					getModifier().isProtected() ||
					getModifier().isTransient() || 
					getModifier().isVolatile()) {
				valid = false;
			}
		} 
		if (!valid) {
			emitError(null, "illegal field modifiers");
		}
	}
	
	private boolean checkPublicPrivateProtected() {
		boolean valid = true;
		if (getModifier().isPrivate() && (getModifier().isPublic() || getModifier().isProtected())) {
			valid = false;
		}
		if (getModifier().isPublic() && (getModifier().isPrivate() || getModifier().isProtected())) {
			valid = false;
		}
		if (getModifier().isProtected() && (getModifier().isPublic() || getModifier().isPrivate())) {
			valid = false;
		}
		return valid;
	}


	@Override
	protected void doVerify() {
		if (typeDescriptor != null) {
			getRoot().checkAndLoadTypeDescriptor(this, descriptorReference, typeDescriptor);
		}
		super.doVerify();
	}


	public void setJavaType(JavaTypeLiteral javaType) {
		this.javaType = javaType;
	}


	@Override
	protected Utf8Info createHighLevelDescriptor() {
		return getConstantPool().getOrAddUtf8nfo(javaType.getDescriptor().getValue());
	}
	
	
	

}
