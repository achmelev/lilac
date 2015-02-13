package org.jasm.item.attribute;

import java.util.HashSet;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class StackMapAttributeContent extends AbstractBytecodeItemList<AbstractStackmapFrame> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "stackmap";
	}

	@Override
	protected AbstractStackmapFrame createEmptyItem(IByteBuffer source,
			long offset) {
		short value = source.readUnsignedByte(offset);
		if (value>=0 && value<=63) {
			return new SameStackmapFrame();
		} else if (value>=64 && value<=127) {
			return new SameLocalsOneStackitemStackmapFrame();
		} else if (value == 247) {
			return new SameLocalsOneStackitemExtendedStackmapFrame();
		} else if (value>=248 && value<=250) {
			return new ChopStackmapFrame();
		} else if (value == 251) {
			return new SameExtendedStackmapFrame();
		} else if (value >= 252 && value <=254) {
			return new AppendStackmapFrame();
		} else if (value == 255) {
			return new FullStackmapFrame();
		} else {
			throw new IllegalArgumentException("unknown tag value: "+value);
		}
	}
	
	

	@Override
	protected void doResolveAfterParse() {
		super.doResolveAfterParse();
		if (!hasErrors()) {
			Set<Integer> targets = new HashSet<Integer>();
			for (int i=0;i<getSize(); i++) {
				AbstractStackmapFrame frame = get(i);
				int index = frame.getInstruction().getIndex();
				if (targets.contains(index)) {
					frame.emitError(null, "double stackmap declaration");
				} else {
					targets.add(index);
				}
				
			}
		}
		
	}

	@Override
	public void prepareRead(int length) {
		
		
	}

	
	

}
