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
	public String getTypeLabel() {
		return  getPrintName();
	}
	
	@Override
	public String getPrintComment() {
		return ((Attribute)getParent()).getName().getValue();
	}
	
	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(((Attribute)getParent()).getName().getSymbolName());
		buf.append(", ");
		buf.append(super.getPrintArgs());
		return buf.toString();
	}
	
	
	 

}
