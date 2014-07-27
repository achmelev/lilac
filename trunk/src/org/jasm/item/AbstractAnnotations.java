package org.jasm.item;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.attribute.Annotation;
import org.jasm.item.attribute.IAttributeContent;

public abstract class AbstractAnnotations extends AbstractBytecodeItemList<Annotation> implements IAttributeContent {
	@Override
	public void prepareRead(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Annotation createEmptyItem(IByteBuffer source, long offset) {
		return new Annotation();
	}
}
