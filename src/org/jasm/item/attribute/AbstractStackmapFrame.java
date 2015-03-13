package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractStackmapFrame extends AbstractByteCodeItem implements IInstructionReference {
	
	protected short tagValue; 
	protected short tagRangeBegin = -1;
	protected int deltaOffset = -1;
	protected SymbolReference instructionReference;
	private AbstractInstruction instruction;
	
	public AbstractStackmapFrame(short tagRangeBegin) {
		this.tagRangeBegin = tagRangeBegin;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		tagValue = source.readUnsignedByte(offset);
		if (offsetCodedInTag()) {
			deltaOffset = (short)(tagValue-tagRangeBegin);
		} else {
			deltaOffset = 0;
		}
		doReadBody(source, offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, calculateTag());
		doWriteBody(target, offset);
	}
	
	private int calculateOffsetFromDeltaOffset() {
		StackMapAttributeContent stackmap = (StackMapAttributeContent)getParent();
		int index = stackmap.indexOf(this);
		if (stackmap.indexOf(this) == 0) {
			return deltaOffset;
		} else {
			AbstractStackmapFrame prev = stackmap.get(index-1);
			return prev.calculateOffsetFromDeltaOffset()+deltaOffset+1;
		}
	}
	
	protected int calculateDeltaOffset() {
		StackMapAttributeContent stackmap = (StackMapAttributeContent)getParent();
		int index = stackmap.indexOf(this);
		if (index == 0) {
			return instruction.getOffsetInCode();
		} else {
			AbstractStackmapFrame prev = stackmap.get(index-1);
			return instruction.getOffsetInCode()-prev.getInstruction().getOffsetInCode()-1;
		}
	}
	
	protected abstract void doReadBody(IByteBuffer source, long offset);
	protected abstract void doWriteBody(IByteBuffer source, long offset);
	

	@Override
	protected void doResolve() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		instruction = code.getInstructions().getInstructionAtOffset(calculateOffsetFromDeltaOffset());
		doResolveBody();
	}

	@Override
	protected void doResolveAfterParse() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		instruction = code.getInstructions().checkAndLoadFromSymbolTable(this, instructionReference);
		StackMapAttributeContent stackmap = (StackMapAttributeContent)getParent();
		int index = stackmap.indexOf(this);
		if (index == 0 && instruction != null) {
			doResolveBodyAfterParse();
		} else if (index > 0) {
			AbstractStackmapFrame prev = stackmap.get(index-1);
			if (instruction != null && prev.getInstruction() != null) {
				doResolveBodyAfterParse();
			}
		}
		
	}
	
	protected abstract void doResolveBody();
	protected abstract void doResolveBodyAfterParse();
	protected abstract boolean offsetCodedInTag();
	protected abstract short calculateTag();

	public AbstractInstruction getInstruction() {
		return instruction;
	}

	protected static AbstractStackmapVariableinfo createEmptyVariableInfo(IByteBuffer source, long offset) {
		short tag = source.readUnsignedByte(offset);
		if (tag == new DoubleStackmapVariableinfo().getTag()) {
			return new DoubleStackmapVariableinfo();
		} else if (tag == new FloatStackmapVariableinfo().getTag()) {
			return new FloatStackmapVariableinfo();
		} else if (tag == new IntegerStackmapVariableinfo().getTag()) {
			return new IntegerStackmapVariableinfo();
		} else if (tag == new LongStackmapVariableinfo().getTag()) {
			return new LongStackmapVariableinfo();
		} else if (tag == new ObjectStackmapVariableinfo().getTag()) {
			return new ObjectStackmapVariableinfo();
		} else if (tag == new UninitializedStackmapVariableinfo().getTag()) {
			return new UninitializedStackmapVariableinfo();
		} else if (tag == new UninitializedThisStackmapVariableinfo().getTag()) {
			return new UninitializedThisStackmapVariableinfo();
		} else if (tag == new NullStackmapVariableinfo().getTag()) {
			return new NullStackmapVariableinfo();
		} else if (tag == new TopStackmapVariableinfo().getTag()) {
				return new TopStackmapVariableinfo();
		} else {
			throw new IllegalArgumentException("unknown tag: "+tag);
		}
	}
	
	protected static AbstractStackmapVariableinfo[] readVariableInfos(IContainerBytecodeItem parent, IByteBuffer source, long offset, int size) {
		AbstractStackmapVariableinfo[] result = new AbstractStackmapVariableinfo[size];
		long currentOffset = offset;
		for (int i=0;i<result.length; i++) {
			result[i] = createEmptyVariableInfo(source, currentOffset);
			result[i].setParent(parent);
			result[i].read(source, currentOffset);
			
			currentOffset+=result[i].getLength();
		}
		return result;
	}
	
	protected static void writeVariableInfos(IByteBuffer target, long offset, AbstractStackmapVariableinfo[] infos) {
		long currentOffset = offset;
		for (int i=0;i<infos.length; i++) {
			infos[i].write(target, currentOffset);
			currentOffset+=infos[i].getLength();
		}
	}
	
	protected int calculateVariableInfosLength(AbstractStackmapVariableinfo[] infos) {
		int length = 0;
		for (AbstractStackmapVariableinfo info: infos) {
			length+=info.getLength();
		}
		return length;
	}

	public void setInstructionReference(SymbolReference instructionReference) {
		this.instructionReference = instructionReference;
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{instruction};
	}

	public void setInstruction(AbstractInstruction instruction) {
		this.instruction = instruction;
	}
	
	
	
	
}
