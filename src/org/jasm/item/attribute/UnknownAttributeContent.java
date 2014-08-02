package org.jasm.item.attribute;



public class UnknownAttributeContent extends AbstractBinaryAttributeContent {

	
	
	public UnknownAttributeContent() {
		super();
	}

	public UnknownAttributeContent(byte[] data) {
		super(data);
	}

	@Override
	public String getPrintName() {
		return "unknown attribute";
	}
	
	@Override
	public String getPrintComment() {
		return ((Attribute)getParent()).getName().getValue();
	}
	
	
	 

}
