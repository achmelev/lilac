package org.jasm.item.attribute;


import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;


public class Attributes extends AbstractBytecodeItemList<Attribute> {

	
	@Override
	public String getPrintName() {
		return "attributes";
	}


	@Override
	protected Attribute createEmptyItem(IByteBuffer source, long offset) {
		return new Attribute();
	}

}
