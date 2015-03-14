package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class FullStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<AbstractStackmapVariableinfo>, IStackmapVariableinfoContainer {
	
	private List<AbstractStackmapVariableinfo> localsList;
	private AbstractStackmapVariableinfo[] locals;
	private List<AbstractStackmapVariableinfo> stackItemsList;
	private AbstractStackmapVariableinfo [] stackItems;
	
	private boolean addToStackItems = false;

	public FullStackmapFrame() {
		super((short)255);
	}

	@Override
	public int getLength() {
		return 1+2+2+2+calculateVariableInfosLength(locals)+calculateVariableInfosLength(stackItems);
	}

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
		return "full";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel()+", "+createItemsListArg(locals)+", "+createItemsListArg(stackItems);
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		long currentOffset = offset+1;
		deltaOffset = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		int number = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		locals = readVariableInfos(this, source, currentOffset, number);
		currentOffset+= calculateVariableInfosLength(locals);
		number = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		stackItems = readVariableInfos(this, source, currentOffset, number);
		
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		long currentOffset = offset+1;
		target.writeUnsignedShort(currentOffset, calculateDeltaOffset());
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, locals.length);
		currentOffset+=2;
		writeVariableInfos(target, currentOffset, locals);
		currentOffset+= calculateVariableInfosLength(locals);
		target.writeUnsignedShort(currentOffset, stackItems.length);
		currentOffset+=2;
		writeVariableInfos(target, currentOffset, stackItems);
		
	}

	@Override
	protected void doResolveBody() {
		for (AbstractStackmapVariableinfo info: locals) {
			info.resolve();
		}
		for (AbstractStackmapVariableinfo info: stackItems) {
			info.resolve();
		}
		
	}
	
	

	@Override
	protected void doVerify() {
		for (AbstractStackmapVariableinfo info: locals) {
			info.verify();
		}
		for (AbstractStackmapVariableinfo info: stackItems) {
			info.verify();
		}
		
	}

	@Override
	protected void doResolveBodyAfterParse() {
		if (calculateDeltaOffset()<0) {
			emitError(instructionReference, "instruction out of allowed interval");
		}
		if (localsList == null) {
			localsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		locals = localsList.toArray(new AbstractStackmapVariableinfo[0]);
		for (AbstractStackmapVariableinfo info: locals) {
			info.resolve();
		}
		if (stackItemsList == null) {
			stackItemsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		stackItems = stackItemsList.toArray(new AbstractStackmapVariableinfo[0]);
		for (AbstractStackmapVariableinfo info: stackItems) {
			info.resolve();
		}
		
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag() {
		return tagRangeBegin;
	}

	@Override
	public int getSize() {
		return locals.length+stackItems.length;
	}

	@Override
	public AbstractStackmapVariableinfo get(int index) {
		if (index<locals.length) {
			return locals[index];
		} else {
			return stackItems[index-locals.length];
		}
		
	}

	@Override
	public int indexOf(AbstractStackmapVariableinfo item) {
		for (int i=0;i<locals.length; i++) {
			if (locals[i] == item) {
				return i;
			}
		}
		for (int i=0;i<stackItems.length; i++) {
			if (stackItems[i] == item) {
				return locals.length+i;
			}
		}
		return -1;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	
	private void addLocal(AbstractStackmapVariableinfo info) {
		if (localsList == null) {
			localsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		info.setParent(this);
		localsList.add(info);
	}
	
	private void addStackItem(AbstractStackmapVariableinfo info) {
		if (stackItemsList == null) {
			stackItemsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		info.setParent(this);
		stackItemsList.add(info);
	}
	
	public void switchAddingToStackItems() {
		addToStackItems = true;
	}

	@Override
	public void addVariableInfo(AbstractStackmapVariableinfo info) {
		if (addToStackItems) {
			addStackItem(info);
		} else {
			addLocal(info);
		}
		
	}

	public AbstractStackmapVariableinfo[] getLocals() {
		return locals;
	}

	public AbstractStackmapVariableinfo[] getStackItems() {
		return stackItems;
	}
	
	
}
