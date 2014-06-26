package org.jasm.item.constantpool;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.AbstractTaggedBytecodeItemList;
import org.jasm.item.IBytecodeItem;

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
	public void resolve() {
		for (IBytecodeItem item: getItems()) {
			((AbstractConstantPoolEntry)item).setParent(this);
		}
		super.resolve();
	}

	@Override
	protected int getSizeDiff() {
		return 1;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IBytecodeItem> getStructureParts() {
		List<IBytecodeItem> result = new ArrayList<>();
		result.addAll(getItems());
		
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "constpool";
	}

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	

	
	
	
	

}
