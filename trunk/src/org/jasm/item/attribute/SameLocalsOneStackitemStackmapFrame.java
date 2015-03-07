package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.instructions.verify.Frame;
import org.jasm.type.verifier.VerifierParams;

public class SameLocalsOneStackitemStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<IBytecodeItem>, IStackmapVariableinfoContainer {
	
	private List<AbstractStackmapVariableinfo> stackitemsList;
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
		return getInstruction().getPrintLabel()+", "+stackitemInfo.getPrintName();
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
	protected void doVerify(VerifierParams params) {
		stackitemInfo.verify(params);
	}

	@Override
	protected void doResolveBodyAfterParse() {
		int tag = calculateTag();
		if (tag<64 || tag>127) {
			emitError(instructionReference, "instruction out of allowed interval");
		}
		if (stackitemsList != null && stackitemsList.size() == 1) {
			stackitemInfo = stackitemsList.get(0);
			stackitemInfo.resolve();
		} else {
			emitError(null, "number of stackitems is out of bounds");
		}
	}

	@Override
	protected boolean offsetCodedInTag() {
		return true;
	}

	@Override
	protected short calculateTag() {
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

	public void setStackitemInfo(AbstractStackmapVariableinfo stackitemInfo) {
		this.stackitemInfo = stackitemInfo;
	}

	@Override
	public void addVariableInfo(AbstractStackmapVariableinfo info) {
		if (stackitemsList == null) {
			stackitemsList = new ArrayList<AbstractStackmapVariableinfo>();
		}
		info.setParent(this);
		stackitemsList.add(info);
	}

	public AbstractStackmapVariableinfo getStackitemInfo() {
		return stackitemInfo;
	}
	
	
}
