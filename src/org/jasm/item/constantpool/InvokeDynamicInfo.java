package org.jasm.item.constantpool;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.BootstrapMethod;
import org.jasm.item.attribute.BootstrapMethodsAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.literals.SymbolReference;

public class InvokeDynamicInfo extends AbstractConstantPoolEntry implements INameReferencingEntry, IDescriptorReferencingEntry {
	
	private int methodIndex;
	private SymbolReference methodReference;
	private BootstrapMethod method;
	private int nameAndTypeIndex;
	private SymbolReference nameAndTypeReference;
	private NameAndTypeInfo nameAndType;
	
	public NameAndTypeInfo getNameAndType() {
		return nameAndType;
	}
	
	public String getName() {
		return getNameAndType().getName();
	}
	
	public String getDescriptor() {
		return getNameAndType().getDescriptor();
	}

	@Override
	public short getTag() {
		return 18;
	}

	@Override
	public int getLength() {
		return 5;
	}

	@Override
	public String getPrintArgs() {
		return method.getSymbolName()+", "+nameAndType.getSymbolName();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	public String getConstTypeLabel() {
		return "dynref";
	}

	@Override
	protected String doGetDisassemblerLabel() {
		if (nameAndType.getNameReference().getDisassemblerLabel() != null) {
			return nameAndType.getNameReference().getDisassemblerLabel()+"_dyn";
		} else {
			return null;
		}
	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		methodIndex = source.readUnsignedShort(offset);
		nameAndTypeIndex = source.readUnsignedShort(offset+2);
	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		int mIndex = getBootstrapMethodsAttributeContent().indexOf(method);
		int ntIndex = nameAndType.getIndexInPool();
		target.writeUnsignedShort(offset, mIndex);
		target.writeUnsignedShort(offset+2, ntIndex);
		
	}

	@Override
	protected void doResolve() {
		method = getBootstrapMethodsAttributeContent().get(methodIndex);
		nameAndType = (NameAndTypeInfo)getConstantPool().get(nameAndTypeIndex-1);
	}
	
	

	@Override
	protected void doVerify() {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		nameAndType = getConstantPool().checkAndLoadFromSymbolTable(this, NameAndTypeInfo.class, nameAndTypeReference);
		if (nameAndType != null && nameAndType.isField()) {
			emitError(nameAndTypeReference, "wrong nameandtype const");
		}
		if (nameAndType != null && (nameAndType.getName().equals("<init>") || nameAndType.getName().equals("<clinit>"))) {
			emitError(nameAndTypeReference, "illegal dynamic callsite name");
		}
		BootstrapMethodsAttributeContent bm = getBootstrapMethodsAttributeContent();
		if (bm != null) {
			method = bm.checkAndLoadFromSymbolTable(this, methodReference);
		} else {
			emitError(methodReference, "unknown bootstrap method "+methodReference.getSymbolName());
		}
	}
	
	private BootstrapMethodsAttributeContent getBootstrapMethodsAttributeContent() {
		
		List<Attribute> attributes =  getAncestor(Clazz.class).getAttributes().
				getAttributesByContentType(BootstrapMethodsAttributeContent.class);
		if (attributes.isEmpty()) {
			return null;
		} else {
			return (BootstrapMethodsAttributeContent)attributes.get(0).getContent();
		}
	}
	
	

	public void setMethodReference(SymbolReference methodReference) {
		this.methodReference = methodReference;
	}

	public void setNameAndTypeReference(SymbolReference nameAndTypeReference) {
		this.nameAndTypeReference = nameAndTypeReference;
	}

	@Override
	public String[] getReferencedNames() {
		return nameAndType.getReferencedNames();
	}

	@Override
	public String[] getReferencedDescriptors() {
		return nameAndType.getReferencedDescriptors();
	}

	@Override
	public void completeGeneratedEntry() {
		// TODO Auto-generated method stub
		
	}
	
	

}
