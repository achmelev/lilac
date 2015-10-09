package org.jasm.resolver;

import org.jasm.item.modifier.FieldModifier;
import org.jasm.item.modifier.MemberModifier;
import org.jasm.type.descriptor.TypeDescriptor;

public class FieldInfo extends AbstractMemberInfo {
	
	private FieldModifier modifier;
	private TypeDescriptor descriptor;
	
	
	public FieldModifier getModifier() {
		return modifier;
	}
	public void setModifier(FieldModifier modifier) {
		this.modifier = modifier;
	}
	public TypeDescriptor getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(TypeDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public MemberModifier getMemberModifier() {
		return getModifier();
	}

}
