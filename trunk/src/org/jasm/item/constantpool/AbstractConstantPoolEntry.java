package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.item.AbstractTaggedBytecodeItem;
import org.jasm.item.IBytecodeItem;

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
	
	
	
	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IBytecodeItem> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintLabel() {
		return "cp"+getParent().indexOf(this);
	}

	protected abstract void doResolve();
	
	
	

}
