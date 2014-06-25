package org.jasm.item.constantpool;

import org.jasm.item.AbstractTaggedBytecodeItem;

public abstract class AbstractConstantPoolEntry extends AbstractTaggedBytecodeItem {
	
	private ConstantPool parent = null;

	public ConstantPool getParent() {
		return parent;
	}

	public void setParent(ConstantPool parent) {
		this.parent = parent;
	}

	@Override
	public void resolve() {
		if (parent == null) {
			throw new RuntimeException("Cannot resolve orphan constant pool entry");
		}
		doResolve();
	}
	
	protected abstract void doResolve();
	
	
	

}
