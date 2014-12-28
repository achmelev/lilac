package org.jasm.resolver;

import org.jasm.item.modifier.MethodModifier;
import org.jasm.type.descriptor.MethodDescriptor;

public class MethodInfo extends AbstractInfo {
	
	private String name;
	private MethodModifier modifier;
	private MethodDescriptor descriptor;
	private ExternalClassInfo parent;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public ExternalClassInfo getParent() {
		return parent;
	}
	public void setParent(ExternalClassInfo parent) {
		this.parent = parent;
	}
	
	

}
