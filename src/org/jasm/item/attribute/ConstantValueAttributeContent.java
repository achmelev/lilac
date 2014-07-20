package org.jasm.item.attribute;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;

public class ConstantValueAttributeContent extends AbstractAttributeContent {
	
	private int valueIndex = -1;
	private IPrimitiveValueReferencingEntry valueEntry = null;
	
	public ConstantValueAttributeContent(IPrimitiveValueReferencingEntry entry) {
		this.valueEntry = entry;
	}
	
	public ConstantValueAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.valueIndex = source.readUnsignedShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, ((AbstractConstantPoolEntry)valueEntry).getIndexInPool());
	}

	@Override
	public int getLength() {
		return 2;
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
		return "constant value";
	}

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(((Attribute)getParent()).getName().getPrintLabel());
		buf.append(", ");
		buf.append(((AbstractConstantPoolEntry)valueEntry).getPrintLabel());
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		buf.append(((Attribute)getParent()).getName().getValue());
		buf.append(", ");
		buf.append(valueEntry.getValue().toString());
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		this.valueEntry = (IPrimitiveValueReferencingEntry)getConstantPool().get(this.valueIndex-1);
	}
	 

}
