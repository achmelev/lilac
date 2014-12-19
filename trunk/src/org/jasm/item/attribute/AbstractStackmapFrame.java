package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractStackmapFrame extends AbstractByteCodeItem {
	
	protected short tagValue; 
	protected short tagRangeBegin = -1;
	protected int deltaOffset = -1;
	private SymbolReference instructionReference;
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
		target.writeUnsignedByte(offset, calculateTag(tagRangeBegin));
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
		if (stackmap.indexOf(this) == 0) {
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
		
	}
	
	protected abstract void doResolveBody();
	protected abstract void doResolveBodyAfterParse();
	protected abstract boolean offsetCodedInTag();
	protected abstract short calculateTag(short tagRangeBegin);

	public AbstractInstruction getInstruction() {
		return instruction;
	}

	protected static AbstractStackmapVariableinfo createEmptyVariableInfo(IByteBuffer source, long offset) {
		short tag = source.readUnsignedByte(offset);
		if (tag == new DoubleVariableinfo().getTag()) {
			return new DoubleVariableinfo();
		} else if (tag == new FloatVariableinfo().getTag()) {
			return new FloatVariableinfo();
		} else if (tag == new IntegerVariableinfo().getTag()) {
			return new IntegerVariableinfo();
		} else if (tag == new LongVariableinfo().getTag()) {
			return new LongVariableinfo();
		} else if (tag == new ObjectVariableinfo().getTag()) {
			return new ObjectVariableinfo();
		} else if (tag == new UninitializedVariableinfo().getTag()) {
			return new UninitializedVariableinfo();
		} else if (tag == new UninitializedThisVariableinfo().getTag()) {
			return new UninitializedThisVariableinfo();
		} else if (tag == new NullVariableinfo().getTag()) {
			return new NullVariableinfo();
		} else if (tag == new TopVariableinfo().getTag()) {
				return new TopVariableinfo();
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
	
}
