package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.verifier.VerifierParams;

public class AnnotationElementNameValue extends AbstractByteCodeItem implements IContainerBytecodeItem<AnnotationElementValue>, IConstantPoolReference {
	
	private int nameIndex = -1;
	private SymbolReference nameReference;
	private Utf8Info name = null;
	private AnnotationElementValue value = null;
	
	public AnnotationElementNameValue() {
		
	}
	
	public AnnotationElementNameValue(Utf8Info name, AnnotationElementValue value) {
		this.name = name;
		this.value = value;
		value.setParent(this);
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		nameIndex = source.readUnsignedShort(offset);
		value = new AnnotationElementValue();
		value.setParent(this);
		value.read(source, offset+2);

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, name.getIndexInPool());
		value.write(target, offset+2);
	}

	@Override
	public int getLength() {
		return 2+value.getLength();
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		result.add(new SimplePrintable(null, "name", name.getSymbolName(), name.getValue()));
		result.add(value);
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "element";
	}
	

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		this.name = (Utf8Info)getConstantPool().get(this.nameIndex-1);
		value.resolve();

	}
	
	@Override
	protected void doVerify(VerifierParams params) {
		value.verify(params);
	}

	@Override
	protected void doResolveAfterParse() {
		this.name = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, nameReference);
		if (name != null) {
			IdentifierUtils.checkSimpleIdentifier(this, nameReference, name);
		}
		value.resolve();
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public AnnotationElementValue get(int index) {
		if (index != 0) {
			throw new IndexOutOfBoundsException();
		}
		return value;
	}

	@Override
	public int indexOf(AnnotationElementValue item) {
		if (value == item) {
			return 0;
		}
		return -1;
	}

	public Utf8Info getName() {
		return name;
	}
	
	public String getNameValue() {
		return name.getValue();
	}

	public AnnotationElementValue getValue() {
		return value;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{name};
	}

	public void setNameReference(SymbolReference nameReference) {
		this.nameReference = nameReference;
	}

	public void setValue(AnnotationElementValue value) {
		value.setParent(this);
		this.value = value;
	}
	
	
	
	
}
