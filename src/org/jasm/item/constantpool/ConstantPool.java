package org.jasm.item.constantpool;

import org.jasm.item.TaggedBytecodeItemList;

public class ConstantPool extends TaggedBytecodeItemList<AbstractConstantPoolEntry> {

	public ConstantPool() {
		super(AbstractConstantPoolEntry.class, "org.jasm.item.constantpool");
	}

}
