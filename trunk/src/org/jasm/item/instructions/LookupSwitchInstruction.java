package org.jasm.item.instructions;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class LookupSwitchInstruction extends AbstractSwitchInstruction {
	
	private int numberOfPairs = -1;
	private int defaultOffset = -1;
	private int[] values = null;
	private int[] targetOffsets = null;
	
	public LookupSwitchInstruction() {
		super(OpCodes.lookupswitch);
	}
	
	public LookupSwitchInstruction(int[] values, AbstractInstruction defaultTarget, AbstractInstruction[] targets) {
		super(OpCodes.lookupswitch);
		if (values.length == 0 || values.length != targets.length) {
			throw new IllegalArgumentException(values.length+"!="+targets.length+" or values.length=0");
		}
		this.values = values;
		this.targets = targets;
		this.defaultTarget = defaultTarget;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		int pad = calculatePad();
		long currentOffset = offset+pad;
		defaultOffset = source.readInt(currentOffset);
		numberOfPairs = source.readInt(currentOffset+4);
		currentOffset+=8;
		values = new int[numberOfPairs];
		targetOffsets = new int[numberOfPairs];
		for (int i=0;i<numberOfPairs; i++) {
			values[i] = source.readInt(currentOffset);
			targetOffsets[i] =  source.readInt(currentOffset+4);
			currentOffset+=8;
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
		target.writeInt(currentOffset+4, targets.length);
		currentOffset+=8;
		for (int i=0;i<targets.length; i++) {
			target.writeInt(currentOffset, values[i]);
			target.writeInt(currentOffset+4, targets[i].getOffsetInCode()-this.getOffsetInCode());
			currentOffset+=8;
		}

	}

	@Override
	public int getLength() {
		return 1+calculatePad()+8+values.length*8;
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
		for (int i=0;i<targets.length; i++) {
			buf.append(values[i]+"->"+targets[i].getPrintLabel());
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
		defaultTarget = instr.getInstructionAtOffset(defaultOffset+this.getOffsetInCode());
		targets = new AbstractInstruction[numberOfPairs];
		for (int i=0;i<numberOfPairs; i++) {
			targets[i] = instr.getInstructionAtOffset(targetOffsets[i]+this.getOffsetInCode());
		}
	}
	
	
	
	private int calculatePad() {
		int offset = getOffsetInCode();
		if ((offset+1)%4 == 0) {
			return 0;
		}
		return 4-(offset+1)%4;
	}

	@Override
	protected void setTargets(AbstractInstruction defaultTarget, int[] values,
			AbstractInstruction[] targets) {
		this.defaultTarget = defaultTarget;
		this.values = values;
		this.targets = targets;
		
	}

	public int[] getValues() {
		return values;
	}


	

}
