package org.jasm.item.clazz;

import org.jasm.bytebuffer.IByteBuffer;

public class Methods extends AbstractClassMemberList<Method> {

	@Override
	public String getPrintName() {
		return "methods";
	}

	@Override
	protected Method createEmptyItem(IByteBuffer source, long offset) {
		return new Method();
	}

}
