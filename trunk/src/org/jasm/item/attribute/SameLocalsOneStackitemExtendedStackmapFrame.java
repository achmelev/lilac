package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.type.verifier.VerifierParams;

public class SameLocalsOneStackitemExtendedStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<IBytecodeItem>, IStackmapVariableinfoContainer {
	
	private List<AbstractStackmapVariableinfo> stackitemsList;
	private AbstractStackmapVariableinfo stackitemInfo;
	
	public SameLocalsOneStackitemExtendedStackmapFrame() {
		super((short)247);
	}

	@Override
	public int getLength() {
		return 1 + 2+ stackitemInfo.getLength();
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
		return "same locals extended";
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
		deltaOffset = source.readUnsignedShort(offset+1);
		stackitemInfo = readVariableInfos(this,source,offset+3,1)[0];
	}
	
	

	@Override
	protected void doVerify(VerifierParams params) {
		
		
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, calculateDeltaOffset());
		stackitemInfo.write(target, offset+3);
	}

	@Override
	protected void doResolveBody() {
		stackitemInfo.resolve();
	}

	@Override
	protected void doResolveBodyAfterParse() {
		if (stackitemsList != null && stackitemsList.size() == 1) {
			stackitemInfo = stackitemsList.get(0);
			stackitemInfo.resolve();
		} else {
			emitError(null, "number of stackitems is out of bounds");
		}
		
		
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag(short tagRangeBegin) {
		return tagRangeBegin;
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

	@Override
	public void addVariableInfo(AbstractStackmapVariableinfo info) {
		if (stackitemsList == null) {
			stackitemsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		info.setParent(this);
		stackitemsList.add(info);
	}

	
	
	
}
