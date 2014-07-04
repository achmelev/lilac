package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.item.AbstractTaggedBytecodeItem;
import org.jasm.item.IBytecodeItem;

public abstract class AbstractConstantPoolEntry extends AbstractTaggedBytecodeItem {


	
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
	
	public int getIndexInPool() {
		return getParent().indexOf(this)+1;
	}

}
