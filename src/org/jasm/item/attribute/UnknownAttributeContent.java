package org.jasm.item.attribute;

import org.apache.commons.codec.binary.Hex;



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
		buf.append("0x"+new String(Hex.encodeHex(getData())));
		return buf.toString();
	}
	
	
	 

}
