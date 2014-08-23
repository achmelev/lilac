package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;
import org.jasm.item.constantpool.StringInfo;

public class ConstantValueAttributeContent extends AbstractSimpleAttributeContent implements IConstantPoolReference {
	
	private int valueIndex = -1;
	private AbstractConstantPoolEntry  valueEntry = null;
	
	public ConstantValueAttributeContent(AbstractConstantPoolEntry entry) {
		this.valueEntry = entry;
		if (!(this.valueEntry instanceof StringInfo || this.valueEntry instanceof IPrimitiveValueReferencingEntry)) {
			throw new IllegalArgumentException(valueEntry+"");
		}
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
		buf.append(((AbstractConstantPoolEntry)valueEntry).getPrintLabel());
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		if (valueEntry instanceof StringInfo) {
			buf.append(((StringInfo)valueEntry).getContent());
		} else {
			buf.append(((IPrimitiveValueReferencingEntry)valueEntry).getValue());
		}
		
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		this.valueEntry = getConstantPool().get(this.valueIndex-1);
	}

	public AbstractConstantPoolEntry getConstantPoolEntry() {
		return (AbstractConstantPoolEntry)valueEntry;
	}
	
	public Object getValue() {
		if (valueEntry instanceof StringInfo) {
			return ((StringInfo)valueEntry).getContent();
		} else {
			return ((IPrimitiveValueReferencingEntry)valueEntry).getValue();
		}
		
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{(AbstractConstantPoolEntry)valueEntry};
	}

}
