package org.jasm.item.attribute;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.Base64Literal;

public abstract class AbstractBinaryAttributeContent extends AbstractSimpleAttributeContent {
	
	private Base64Literal dataLiteral;
	private byte [] data = null;
	
	protected AbstractBinaryAttributeContent() {
		
	}
	
	public AbstractBinaryAttributeContent(byte[] data) {
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
	public String getPrintArgs() {
		return "["+Base64.encodeBase64String(data)+"]";
	}


	@Override
	protected void doResolve() {

	}
	
	@Override
	protected void doResolveAfterParse() {
		data = dataLiteral.getValue();
		if (data == null) {
			emitError(dataLiteral, "invalid base64 literal");
		}
	}

	public byte[] getData() {
		return data;
	}

	public void setDataLiteral(Base64Literal dataLiteral) {
		this.dataLiteral = dataLiteral;
	}
	 
	

}
