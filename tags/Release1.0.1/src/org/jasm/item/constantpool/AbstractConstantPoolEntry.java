package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractTaggedBytecodeItem;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.literals.Label;

public abstract class AbstractConstantPoolEntry extends AbstractTaggedBytecodeItem implements ISymbolTableEntry {

	private Label label = null;
	private String disassemblerLabel = null;
	
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
		return "const "+getConstTypeLabel()+" "+getSymbolName();
	}

	public int getIndexInPool() {
		return getParent().indexOf(this)+1;
	}	

	@Override
	public String getSymbolName() {
		if (label != null) {
			return label.getLabel();
		} else if (getDisassemblerLabel() != null) {
			return getDisassemblerLabel();
		} else {
			return createConstName();
		}
		
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}
	
	protected String createConstName() {
		String name = getConstTypeLabel();
		return name+"_"+getParent().indexOf(this);
	}
	
	public String getDisassemblerLabel() {

		if (disassemblerLabel == null) {
			disassemblerLabel = doGetDisassemblerLabel();
			if (disassemblerLabel == null) {
				disassemblerLabel = createConstName();
				return disassemblerLabel;
			}
			if (disassemblerLabel.indexOf("<init>")>=0) {
				disassemblerLabel = disassemblerLabel.replace("<init>", "init0");
			}
			if (disassemblerLabel.indexOf("<clinit>")>=0) {
				disassemblerLabel = disassemblerLabel.replace("<clinit>", "clinit0");
			}
			if (IdentifierUtils.isValidIdentifier(disassemblerLabel)) {
				disassemblerLabel = getConstantPool().getConstNameGenerator().generateName(disassemblerLabel); 
			} else {
				disassemblerLabel = createConstName();
			}
		} 
		return disassemblerLabel;
	}
	
	
	
	public void setDisassemblerLabel(String disassemblerLabel) {
		this.disassemblerLabel = disassemblerLabel;
	}

	public abstract String getConstTypeLabel();
	protected abstract String doGetDisassemblerLabel();

}
