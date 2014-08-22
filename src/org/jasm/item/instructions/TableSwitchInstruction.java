package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class TableSwitchInstruction extends AbstractInstruction {
	
	private int low = -1;
	private int high = -1;
	private int defaultOffset = -1;
	private AbstractInstruction defaultTarget = null;
	private int[] targetOffsets = null;
	private AbstractInstruction[] targets = null;
	
	public TableSwitchInstruction() {
		super(OpCodes.tableswitch);
	}
	
	public TableSwitchInstruction(int low, int high, AbstractInstruction defaultTarget, AbstractInstruction[] targets) {
		super(OpCodes.tableswitch);
		if ((low>high) || (high-low+1) != targets.length) {
			throw new IllegalArgumentException(low+":"+high+":"+targets.length);
		}
		this.defaultTarget = defaultTarget;
		this.low = low;
		this.high = high;
		this.targets = targets;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		int pad = calculatePad();
		long currentOffset = offset+pad;
		defaultOffset = source.readInt(currentOffset);
		low= source.readInt(currentOffset+4);
		high= source.readInt(currentOffset+8);
		currentOffset+=12;
		targetOffsets = new int[high-low+1];
		for (int i=0;i<(high-low+1); i++) {
			targetOffsets[i] =  source.readInt(currentOffset);
			currentOffset+=4;
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		int pad = calculatePad();
		for (int i=0;i<pad; i++) {
			target.writeByte(offset+i, (byte)0);
		}
		long currentOffset = offset+pad;
		target.writeInt(currentOffset, defaultTarget.getOffsetInCode()-this.getOffsetInCode());
		target.writeInt(currentOffset+4, low);
		target.writeInt(currentOffset+8, high);
		currentOffset+=12;
		for (int i=0;i<targets.length; i++) {
			target.writeInt(currentOffset, targets[i].getOffsetInCode()-this.getOffsetInCode());
			currentOffset+=4;
		}

	}

	@Override
	public int getLength() {
		return 1+calculatePad()+12+(high-low+1)*4;
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
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		for (int i=low;i<high+1; i++) {
			buf.append(i+"->"+targets[i-low].getPrintLabel());
			buf.append(",");
		}
		buf.append("default->"+defaultTarget.getPrintLabel());
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		Instructions instr = (Instructions)getParent();
		defaultTarget = instr.getInstructionAtOffset(defaultOffset+getOffsetInCode());
		targets = new AbstractInstruction[high-low+1];
		for (int i=0;i<high-low+1; i++) {
			targets[i] = instr.getInstructionAtOffset(targetOffsets[i]+getOffsetInCode());
		}
	}
	
	private int calculatePad() {
		int offset = getOffsetInCode();
		if ((offset+1)%4 == 0) {
			return 0;
		}
		return 4-(offset+1)%4;
	}

}
