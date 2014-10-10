package org.jasm.item.attribute;


import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;


public class Attributes extends AbstractBytecodeItemList<Attribute> {

	
	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  "attributes";
	}


	@Override
	protected Attribute createEmptyItem(IByteBuffer source, long offset) {
		return new Attribute();
	}


	@Override
	public String getPrintComment() {
		return null;	
	}
	
	public <U extends IAttributeContent> List<Attribute> getAttributesByContentType(Class<U> clazz) {
		List<Attribute> result = new ArrayList<>();
		for (Attribute attr: getItems()) {
			if (attr.getContent().getClass().equals(clazz)) {
				result.add(attr);
			}
		}
		return result;
	}

}
