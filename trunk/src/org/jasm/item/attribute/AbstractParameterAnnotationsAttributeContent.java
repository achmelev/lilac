package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public abstract class AbstractParameterAnnotationsAttributeContent extends AbstractBytecodeItemList<ParameterAnnotations> implements IAttributeContent {
	@Override
	public void prepareRead(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ParameterAnnotations createEmptyItem(IByteBuffer source, long offset) {
		return new ParameterAnnotations();
	}

	@Override
	protected int sizeFieldLength() {
		return 1;
	}
	
	
}
