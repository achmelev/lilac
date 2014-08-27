package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractTaggedBytecodeItem;
import org.jasm.parser.ISymbolTableEntry;

public abstract class AbstractConstantPoolEntry extends AbstractTaggedBytecodeItem implements ISymbolTableEntry {

	private String label = null;
	
	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintLabel() {
		return "cp"+getParent().indexOf(this);
	}	
	
	public int getIndexInPool() {
		return getParent().indexOf(this)+1;
	}

	@Override
	public String getSymbolTypeLabel() {
		return getPrintName();
	}
	
	

	@Override
	public String getSymbolName() {
		return getLabel();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	
	

}
