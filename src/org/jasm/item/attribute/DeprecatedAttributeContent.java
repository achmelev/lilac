package org.jasm.item.attribute;

public class DeprecatedAttributeContent extends AbstractEmptyAttributeContent {

	@Override
	public String getPrintName() {
		return "deprecated";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

}
