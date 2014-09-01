package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class ParameterAnnotations extends AbstractBytecodeItemList<Annotation> {

	@Override
	public String getPrintName() {
		StringBuffer buf = new StringBuffer();
		if (this.getParent() instanceof RuntimeInvisibleParameterAnnotationsAttributeContent) {
			buf.append("invisible ");
		} 
		buf.append("parameter annotations");
		return  buf.toString();
	}

	@Override
	protected Annotation createEmptyItem(IByteBuffer source, long offset) {
		return new Annotation();
	}

}
