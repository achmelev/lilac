package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IUtf8ConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractStringAttributeContent extends AbstractSimpleAttributeContent implements IUtf8ConstantPoolReference {
	
	private int valueIndex = -1;
	private SymbolReference valueLabel;
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
		buf.append(valueEntry.getSymbolName());
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
	
	@Override
	protected void doResolveAfterParse() {
		valueEntry = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, valueLabel);
	}

	public Utf8Info getValueEntry() {
		return valueEntry;
	}

	public String getValue() {
		return valueEntry.getValue();
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{valueEntry};
	}

	public SymbolReference getValueLabel() {
		return valueLabel;
	}

	public void setValueLabel(SymbolReference valueLabel) {
		this.valueLabel = valueLabel;
	}

	@Override
	public String generateName(Utf8Info utf8) {
		if (getPrintName() != null) {
			return getPrintName().replace(' ', '_')+"_name";
		} else {
			return null;
		}
	}

	
	
	

}
