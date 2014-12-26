package org.jasm.item.classpath;

import org.jasm.item.modifier.FieldModifier;
import org.jasm.type.descriptor.TypeDescriptor;

public class FieldInfo {
	
	private String name;
	private FieldModifier modifier;
	private TypeDescriptor descriptor;
	private ExternalClassInfo parent;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public ExternalClassInfo getParent() {
		return parent;
	}
	public void setParent(ExternalClassInfo parent) {
		this.parent = parent;
	}
	
	

}
