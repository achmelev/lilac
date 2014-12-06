package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractTaggedBytecodeItem;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.literals.Label;

public abstract class AbstractConstantPoolEntry extends AbstractTaggedBytecodeItem implements ISymbolTableEntry {

	private Label label = null;
	
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
		return null;
	}	
	
	
	
	@Override
	public String getPrintName() {
		return getTypeLabel()+" "+createConstName();
	}

	public int getIndexInPool() {
		return getParent().indexOf(this)+1;
	}	

	@Override
	public String getSymbolName() {
		if (label == null) {
			return createConstName();
		}
		return label.getLabel();
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
	protected String createConstName() {
		String name = getTypeLabel().trim();
		if (name.startsWith("const")) {
			name = name.substring("const".length(),name.length()).trim();
		}
		return name+"_"+getParent().indexOf(this);
	}
	
	

	
	
	

}
