package org.jasm.item.constantpool;

import org.jasm.item.AbstractTaggedBytecodeItemList;

public class ConstantPool extends AbstractTaggedBytecodeItemList<AbstractConstantPoolEntry> {

	public ConstantPool() {
		super(AbstractConstantPoolEntry.class, "org.jasm.item.constantpool");
	}

	@Override
	public void add(AbstractConstantPoolEntry item) {
		item.setParent(this);
		super.add(item);
	}

	@Override
	public void add(int index, AbstractConstantPoolEntry item) {
		item.setParent(this);
		super.add(index, item);
	}

	@Override
	protected int getSizeDiff() {
		return 1;
	}

	

	
	
	
	

}
