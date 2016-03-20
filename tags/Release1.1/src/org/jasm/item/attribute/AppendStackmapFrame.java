package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class AppendStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<AbstractStackmapVariableinfo>, IStackmapVariableinfoContainer {
	
	
	private List<AbstractStackmapVariableinfo> localsList;
	private AbstractStackmapVariableinfo[] locals;
	
	public AppendStackmapFrame() {
		super((short)252);
	}

	@Override
	public int getLength() {
		return 1+2+calculateVariableInfosLength(locals);
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
		return "append";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel()+", "+createItemsListArg(locals);
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		deltaOffset = source.readUnsignedShort(offset+1);
		locals = readVariableInfos(this, source, offset+3, tagValue-251);
		
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, calculateDeltaOffset());
		writeVariableInfos(target, offset+3, locals);
	}

	@Override
	protected void doResolveBody() {
		for (AbstractStackmapVariableinfo info: locals) {
			info.resolve();
		}
	}
	
	

	@Override
	protected void doVerify() {
		for (AbstractStackmapVariableinfo info: locals) {
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
		
		if (calculateTag()<252 || calculateTag()>254) {
			emitError(null, "number of locals out of bounds");
		}
		
		for (AbstractStackmapVariableinfo info: locals) {
			info.resolve();
		}
		
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag() {
		return (short)(251+locals.length);
	}

	@Override
	public int getSize() {
		return locals.length;
	}

	@Override
	public AbstractStackmapVariableinfo get(int index) {
		return locals[index];
	}

	@Override
	public int indexOf(AbstractStackmapVariableinfo item) {
		for (int i=0;i<locals.length; i++) {
			if (locals[i] == item) {
				return i;
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

	@Override
	public void addVariableInfo(AbstractStackmapVariableinfo info) {
		addLocal(info);
		
	}

	public AbstractStackmapVariableinfo[] getLocals() {
		return locals;
	}
	
	

}
