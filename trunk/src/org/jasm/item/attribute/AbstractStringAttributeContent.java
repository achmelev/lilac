package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;
import org.jasm.item.constantpool.Utf8Info;

public abstract class AbstractStringAttributeContent extends AbstractSimpleAttributeContent implements IConstantPoolReference {
	
	private int valueIndex = -1;
	private Utf8Info valueEntry = null;
	
	public AbstractStringAttributeContent(Utf8Info entry) {
		this.valueEntry = entry;
	}
	
	public AbstractStringAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.valueIndex = source.readUnsignedShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, valueEntry.getIndexInPool());
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
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(valueEntry.getPrintLabel());
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		buf.append(valueEntry.getValue().toString());
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		this.valueEntry = (Utf8Info)getConstantPool().get(this.valueIndex-1);
	}

	public Utf8Info getValueEntry() {
		return valueEntry;
	}

	public Object getValue() {
		return valueEntry.getValue();
	}

	@Override
	public AbstractConstantPoolEntry[] getReference() {
		return new AbstractConstantPoolEntry[]{valueEntry};
	}
	
	

}
