package org.jasm.item.clazz;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IUtf8ConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.AbstractClassMemberModifier;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractClassMember<T extends AbstractClassMemberModifier> extends AbstractByteCodeItem implements IContainerBytecodeItem<Attributes>, IUtf8ConstantPoolReference, IAttributesContainer {
	
	private List<Keyword> modifierLiterals;
	protected T modifier = null;
	private Utf8Info name = null;
	private SymbolReference nameReference;
	private int nameIndex = -1;
	private Utf8Info descriptor = null;
	protected SymbolReference descriptorReference;
	private int descriptorIndex = -1;
	private Attributes attributes = null;
	
	protected boolean highLevelSyntax = false;
	private SymbolReference highLevelNameReference;
	
	public AbstractClassMember() {
		initChildren();
	}
	
	
	@Override
	public void read(IByteBuffer source, long offset) {
		this.modifier = createModifier(source.readUnsignedShort(offset));
		this.nameIndex = source.readUnsignedShort(offset+2);
		this.descriptorIndex = source.readUnsignedShort(offset+4);
		attributes.read(source, offset+6);
	}
	

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, modifier.getValue());
		target.writeUnsignedShort(offset+2, getConstantPool().indexOf(name)+1);
		target.writeUnsignedShort(offset+4, getConstantPool().indexOf(descriptor)+1);
		attributes.write(target, offset+6);
	}

	@Override
	public int getLength() {
		return 6+attributes.getLength();
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<IPrintable>();
		result.add(new SimplePrintable(null, "name", new String[]{name.getSymbolName()}, name.getValue()));
		result.add(new SimplePrintable(null, "descriptor", new String[]{descriptor.getSymbolName()}, descriptor.getValue()));
		
		result.add(attributes);
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
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
		this.descriptor = (Utf8Info)getConstantPool().get(this.descriptorIndex-1);
		attributes.resolve();
	}
	
	@Override
	protected void doVerify() {
		attributes.verify();
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (this.nameReference != null) {
			if (this.highLevelSyntax) {
				emitError(nameReference, "unexpected name statement");
			} else {
				this.name = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, nameReference);
				if (this.name != null) {
					verifyName(nameReference, name);
				}
			}	
		} else {
			if (!this.highLevelSyntax) {
				emitError(null, "missing name statement");
			}	
		}
		if (this.descriptorReference != null) {
			if (this.highLevelSyntax) {
				emitError(nameReference, "unexpected descriptor statement");
			} else {
				this.descriptor = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, descriptorReference);
				if (this.descriptor != null) {
					verifyDescriptor(descriptorReference,descriptor.getValue());
				}
			}	
		} else {
			if (!this.highLevelSyntax) {
				emitError(null, "missing descriptor statement");
			}	
		}
		
		if (!this.hasErrors() && this.highLevelSyntax) {
			if (highLevelNameReference.getContent().indexOf('.')>=0) {
				emitError(highLevelNameReference, "malformed field or method name");
			} else {
				this.name = getConstantPool().getOrAddUtf8nfo(highLevelNameReference.getContent());
				this.descriptor = createHighLevelDescriptor();
			}	
			
		}
		
		if (!this.hasErrors()) {
			modifier = createModifier(0);
			for (Keyword kw: modifierLiterals) {
				modifier.setFlag(kw.getKeyword());
			}
			checkModifiers();
			if (!hasErrors()) {
				attributes.resolve();
			}
		}
		
	}
	
	public Attributes getAttributes() {
		return this.attributes;
	}
	

	public T getModifier() {
		return modifier;
	}


	public Utf8Info getName() {
		return name;
	}

	public void setName(Utf8Info name) {
		this.name = name;
	}

	public Utf8Info getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(Utf8Info descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public Attributes get(int index) {
		if (index != 0) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return attributes;
	}

	@Override
	public int indexOf(Attributes item) {
		if (item != attributes) {
			return -1;
		}
		return 0;
	}
	
	protected abstract T createModifier(int index);
	
	private void initChildren() {
		attributes = new Attributes();
		attributes.setParent(this);
		modifierLiterals = new ArrayList<>();
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{name, descriptor};
	}

	public void setNameReference(SymbolReference nameReference) {
		this.nameReference = nameReference;
	}
	
	public SymbolReference getNameReference() {
		return nameReference;
	}

	public void setDescriptorReference(SymbolReference descriptorReference) {
		this.descriptorReference = descriptorReference;
	}
	
	public SymbolReference getDescriptorReference() {
		return descriptorReference;
	}

	public List<Keyword> getModifierLiterals() {
		return modifierLiterals;
	}
	
	protected abstract void verifyName(SymbolReference ref, Utf8Info name);
	protected abstract void verifyDescriptor(SymbolReference ref, String descriptor);
	protected abstract void checkModifiers();
	protected abstract Utf8Info createHighLevelDescriptor();
	
	@Override
	public String generateName(Utf8Info utf8) {
		if (utf8 == name) {
			return name.getValue()+"_name";
		} else if (utf8 == descriptor) {
			return name.getValue()+"_desc";
		} else {
			return null;
		}
	}


	public void setHighLevelSyntax(boolean highLevelSyntax) {
		this.highLevelSyntax = highLevelSyntax;
	}
	
	public boolean isHighLevelSyntax() {
		return highLevelSyntax;
	}

	public void setHighLevelNameReference(SymbolReference highLevelNameReference) {
		this.highLevelNameReference = highLevelNameReference;
	}	
	
	

}
