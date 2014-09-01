package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;

public class ExceptionHandlerTable extends AbstractBytecodeItemList<ExceptionHandler> {

	@Override
	public String getPrintName() {
		return null;
	}


	@Override
	protected ExceptionHandler createEmptyItem(IByteBuffer source, long offset) {
		return new ExceptionHandler();
	}

}
