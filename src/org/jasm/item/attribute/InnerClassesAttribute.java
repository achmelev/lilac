package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class InnerClassesAttribute extends AbstractBytecodeItemList<InnerClass> implements IAttributeContent {
	
	
	@Override
	public String getPrintName() {
		return "inner classes";
	}

	@Override
	protected InnerClass createEmptyItem(IByteBuffer source, long offset) {
		return new InnerClass();
	}

	@Override
	public void prepareRead(int length) {
		
	}

}
