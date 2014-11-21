package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;




public class RuntimeVisibleTypeAnnotationsAttributeContent extends
		AbstractAnnotationsAttributeContent {

	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  "type annotations";
	}

	@Override
	protected Annotation createEmptyItem(IByteBuffer source, long offset) {
		Annotation result = super.createEmptyItem(source, offset); 
		result.setTypeAnnotation(true);
		return result;
	}
	
	

	

}
