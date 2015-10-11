package org.jasm.item.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class TableSwitchInstruction extends AbstractSwitchInstruction {
	
	private int low = -1;
	private int high = -1;
	private int defaultOffset = -1;
	private int[] targetOffsets = null;
	
	public TableSwitchInstruction() {
		super(OpCodes.tableswitch);
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
	
	private int calculatedPad = -1;
	
	private int calculatePad() {
		if (calculatedPad >=0) {
			return calculatedPad;
		} else {
			calculatedPad = doCalculatePad();
			return calculatedPad;
		}
	}
	
	private int doCalculatePad() {
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
		if (values.length == 0) {
			emitError(null, "there has to be at least one non-default target");
		} else if (values.length==1) {
			low = values[0];
			high = values[0];
			this.targets = targets;
		} else {
			Map<Integer, AbstractInstruction> targetsMap = new HashMap<>();
			List<Integer> valuesList= new ArrayList<>();
			for (int i=0; i<values.length; i++) {
				valuesList.add(values[i]);
				targetsMap.put(values[i], targets[i]);
			}
			Collections.sort(valuesList);
			if (valuesList.size()!=(valuesList.get(valuesList.size()-1)-valuesList.get(0)+1)) {
				emitError(null, "missing target for at least one value between "+valuesList.get(0)+" and "+valuesList.get(valuesList.size()-1));
			} else {
				this.low = valuesList.get(0);
				this.high = valuesList.get(valuesList.size()-1);
				this.targets = new AbstractInstruction[valuesList.size()];
				for (int i=0;i<valuesList.size(); i++) {
					this.targets[i] = targetsMap.get(valuesList.get(i));
				}
			}
		}
		
	}
	
	public int getLow() {
		return low;
	}

	public int getHigh() {
		return high;
	}

	
}
