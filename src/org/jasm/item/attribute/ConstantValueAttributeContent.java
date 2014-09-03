package org.jasm.item.attribute;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.parser.literals.SymbolReference;

public class ConstantValueAttributeContent extends AbstractSimpleAttributeContent implements IConstantPoolReference {
	
	private int valueIndex = -1;
	private AbstractConstantPoolEntry  valueEntry = null;
	private SymbolReference valueReference;
	
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
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(((AbstractConstantPoolEntry)valueEntry).getSymbolName());
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
	
	@Override
	protected void doResolveAfterParse() {
		valueEntry = getConstantPool().checkAndLoadFromSymbolTable(new Class[]{StringInfo.class,IntegerInfo.class,LongInfo.class,FloatInfo.class,DoubleInfo.class}, valueReference);
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

	public void setValueReference(SymbolReference valueReference) {
		this.valueReference = valueReference;
	}
	
	
}
