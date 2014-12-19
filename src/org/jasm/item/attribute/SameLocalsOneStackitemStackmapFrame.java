package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class SameLocalsOneStackitemStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<IBytecodeItem> {
	
	private AbstractStackmapVariableinfo stackitemInfo;
	
	public SameLocalsOneStackitemStackmapFrame() {
		super((short)64);
	}

	@Override
	public int getLength() {
		return 1 + stackitemInfo.getLength();
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
		return "same locals";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel()+", "+createItemsListArg(new AbstractByteCodeItem[]{stackitemInfo});
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		stackitemInfo = readVariableInfos(this,source,offset+1,1)[0];
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		writeVariableInfos(target, offset+1, new AbstractStackmapVariableinfo[]{stackitemInfo});
	}

	@Override
	protected void doResolveBody() {
		stackitemInfo.resolve();
	}

	@Override
	protected void doResolveBodyAfterParse() {
		stackitemInfo.resolve();
	}

	@Override
	protected boolean offsetCodedInTag() {
		return true;
	}

	@Override
	protected short calculateTag(short tagRangeBegin) {
		return (short)(tagRangeBegin+calculateDeltaOffset());
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public IBytecodeItem get(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException(""+index);
		} else {
			return stackitemInfo;
		}
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		if (item == stackitemInfo) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	

}
