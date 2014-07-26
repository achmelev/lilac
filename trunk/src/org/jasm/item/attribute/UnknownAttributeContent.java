package org.jasm.item.attribute;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class UnknownAttributeContent extends AbstractSimpleAttributeContent {
	
	private byte [] data = null;
	
	public UnknownAttributeContent() {
		
	}
	
	public UnknownAttributeContent(byte[] data) {
		this.data = data;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		data = source.readByteArray(offset, getLengthToRead());
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeByteArray(offset, data);
	}

	@Override
	public int getLength() {
		return data.length;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}
	
	

	
	@Override
	public String getPrintName() {
		return "unknown attribute";
	}

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append("0x"+new String(Hex.encodeHex(data)));
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		return ((Attribute)getParent()).getName().getValue();
	}

	@Override
	protected void doResolve() {

	}
	 

}
