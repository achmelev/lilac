package org.jasm.item.attribute;

public class SynteticAttributeContent extends AbstractEmptyAttributeContent {

	@Override
	public String getPrintName() {
		return "synthetic";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

}
