package org.jasm.item.clazz;

import org.jasm.bytebuffer.IByteBuffer;

public class Fields extends AbstractClassMemberList<Field> {

	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getPrintComment() {
		return "Fields";
	}



	@Override
	protected Field createEmptyItem(IByteBuffer source, long offset) {
		return new Field();
	}
	
	public Field getField(String name, String descriptor) {
		return getMember(name, descriptor);
	}

}
