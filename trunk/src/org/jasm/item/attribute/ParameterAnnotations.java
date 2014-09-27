package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class ParameterAnnotations extends AbstractBytecodeItemList<Annotation> {

	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  "parameter annotations";
	}

	@Override
	protected Annotation createEmptyItem(IByteBuffer source, long offset) {
		return new Annotation();
	}

}
