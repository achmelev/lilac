package org.jasm.item.clazz;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.MethodModifier;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.MethodDescriptor;

public class Method extends AbstractClassMember<MethodModifier> {
	
	private MethodDescriptor methodDescriptor =null;
	
	private boolean generateStackMap;
	
	public Method() {
		
	}

	@Override
	public String getPrintName() {
		if (!modifier.hasNoFlags()) {
			return modifier.toString()+" method";
		} else {
			return "method";
		}
	}
	
	

	@Override
	public String getPrintComment() {
		if (modifier.isAbstract() || modifier.isNative()) {
			return null;
		}
		if (getRoot().getMajorVersion()<50) {
			return null;
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append("NOTE: If you modify the code of method and the resulting code contains branch instructions,\n");
		buf.append("you'll probably have to modify or create a stackmap as well.\n");
		buf.append("You might use the short variant of the stackmap statement (just the keyword stackmap followed by a semicolon),\n"); 
	    buf.append("this instructs the assembler to generate a stackmap for you, for this to work however the code verification must be enabled");
		
	    return buf.toString();
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
	protected void verifyName(SymbolReference ref, Utf8Info name) {
		IdentifierUtils.checkMethodName(this, ref, name);
	}

	public MethodDescriptor getMethodDescriptor() {
		return methodDescriptor;
	}

	@Override
	public String toString() {
		if (getName() != null && methodDescriptor != null) {
			return super.toString()+"_"+getName().getValue();
		} else {
			return super.toString();
		}
		
	}
	
	
	
	protected void checkModifiers() {
		if (getName().getValue().equals("<clinit>")) {
			return;
		}
		boolean valid = true;
		boolean istInit = getName().getValue().equals("<init>");
		boolean isInInterface = getRoot().getModifier().isInterface();
		if (!isInInterface && !istInit) {
			valid = checkPublicPrivateProtected();
		} else if (isInInterface) {
			if (getModifier().isProtected() || getModifier().isFinal() || getModifier().isSynchronized() || getModifier().isNative()) {
				valid = false;
			} 
			if (classVersionLess("52.0")) {
				if (!(getModifier().isPublic() && getModifier().isAbstract())) {
					valid = false;
				}
			} else {
				valid = checkPublicPrivateProtected();
			}
		} else if (istInit) {
			valid = checkPublicPrivateProtected();
			if (getModifier().isBridge() || getModifier().isFinal() || 
					getModifier().isNative() || getModifier().isStatic() ||
					getModifier().isSynchronized()) {
				valid = false;
			}	
		}
		if (getModifier().isAbstract()) {
			if (getModifier().isPrivate() || getModifier().isStatic() || 
					getModifier().isSynchronized()  ||
					getModifier().isStrict()) {
				valid = false;
			}
		}
		if (!valid) {
			emitError(null, "illegal method modifiers");
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
		
		if (methodDescriptor != null) {
			getRoot().checkAndLoadMethodDescriptor(this, descriptorReference, methodDescriptor);
			String name = getName().getValue();
			if (name.equals("<init>") && !methodDescriptor.isVoid()) {
				emitError(descriptorReference, "constructor with illegal signature");
			}
			if (name.equals("<clinit>") && !(methodDescriptor.isVoid() && methodDescriptor.getParameters().size()==0)) {
				emitError(descriptorReference, "class or interface initialization method with illegal signature");
			}
			if (name.equals("<clinit>") && (getRoot().getDecimalVersion().doubleValue()>=51.0) && !getModifier().isStatic()) {
				emitError(descriptorReference, "class or interface initialization method must be static");
			}
			super.doVerify();
		}
		
	}

	@Override
	protected void doResolve() {
		super.doResolve();
		methodDescriptor = new MethodDescriptor(getDescriptor().getValue());
	}

	public void setGenerateStackMap(boolean generateStackMap) {
		this.generateStackMap = generateStackMap;
	}

	public boolean isGenerateStackMap() {
		return generateStackMap;
	}

	@Override
	protected Utf8Info createHighLevelDescriptor() {
		throw new NotImplementedException("");
	}
	
	
	

}
