package org.jasm.item.constantpool;

public abstract class AbstractRefInfo extends AbstractReferenceEntry {
	
	public AbstractRefInfo() {
		
	}
	
	public AbstractRefInfo(ClassInfo clazz, NameAndTypeInfo nameAndType) {
		AbstractConstantPoolEntry[] value = new AbstractConstantPoolEntry[2];
		value[0] = clazz;
		value[1] = nameAndType;
		setReference(value);
	}

	

	@Override
	protected int getNumberOfReferences() {
		return 2;
	}
	
	public ClassInfo getClassReference() {
		return (ClassInfo)getReference()[1];
	}
	
	public String getClassName() {
		return getClassReference().getClassName();
	}
	
	public NameAndTypeInfo getNameAndTypeReference() {
		return (NameAndTypeInfo)getReference()[1];
	}
	
	public String getName() {
		return getNameAndTypeReference().getName();
	}
	
	public String getDescriptor() {
		return getNameAndTypeReference().getDescriptor();
	}

}
