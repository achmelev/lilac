package org.jasm.resolver;

import org.jasm.item.modifier.MemberModifier;
import org.jasm.item.modifier.MethodModifier;
import org.jasm.type.descriptor.MethodDescriptor;

public class MethodInfo extends AbstractMemberInfo {
	
	private MethodModifier modifier;
	private MethodDescriptor descriptor;
	private boolean polymorphic = false;
	
	
	public MethodModifier getModifier() {
		return modifier;
	}
	public void setModifier(MethodModifier modifier) {
		this.modifier = modifier;
	}
	public MethodDescriptor getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(MethodDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	public boolean isPolymorphic() {
		return polymorphic;
	}
	public void setPolymorphic(boolean polymorphic) {
		this.polymorphic = polymorphic;
	}
	@Override
	public MemberModifier getMemberModifier() {
		return getModifier();
	}
	
	

}
