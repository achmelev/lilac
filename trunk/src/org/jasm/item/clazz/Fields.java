package org.jasm.item.clazz;

import org.jasm.bytebuffer.IByteBuffer;

public class Fields extends AbstractClassMemberList<Field> {

	@Override
	public String getPrintName() {
		return "fields";
	}

	@Override
	protected Field createEmptyItem(IByteBuffer source, long offset) {
		return new Field();
	}

}
