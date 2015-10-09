package org.jasm.resolver;

import org.jasm.item.modifier.MemberModifier;

public abstract class AbstractMemberInfo extends AbstractInfo {
	
	private String name;
	private ExternalClassInfo parent;
	
	public abstract MemberModifier getMemberModifier();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ExternalClassInfo getParent() {
		return parent;
	}
	
	public void setParent(ExternalClassInfo parent) {
		this.parent = parent;
	}
}
