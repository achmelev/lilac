package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public abstract class AbstractAnnotationsAttributeContent extends AbstractBytecodeItemList<Annotation> implements IAttributeContent {
	@Override
	public void prepareRead(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Annotation createEmptyItem(IByteBuffer source, long offset) {
		return new Annotation();
	}
}
