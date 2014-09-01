package org.jasm.item.clazz;

import org.jasm.bytebuffer.IByteBuffer;

public class Methods extends AbstractClassMemberList<Method> {

	@Override
	public String getPrintName() {
		return null;
	}

	@Override
	protected Method createEmptyItem(IByteBuffer source, long offset) {
		return new Method();
	}
	
	public Method getMethod(String name, String descriptor) {
		return getMember(name, descriptor);
	}

}