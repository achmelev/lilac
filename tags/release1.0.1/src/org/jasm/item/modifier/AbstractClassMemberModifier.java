package org.jasm.item.modifier;

public abstract class AbstractClassMemberModifier extends AbstractModifier {

	public AbstractClassMemberModifier(int value) {
		super(value);
	}
	
	public abstract void setFlag(String value);


}
