package org.jasm.item.attribute;

public class StackMapAttributeContent extends AbstractBinaryAttributeContent {

	@Override
	public String getPrintName() {
		return "stackmap";
	}

	@Override
	public String getPrintComment() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

}
