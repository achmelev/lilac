package org.jasm.item.modifier;

public interface MemberModifier {

	public abstract boolean isPublic();

	public abstract boolean isPrivate();

	public abstract boolean isProtected();

	public abstract boolean isStatic();

}