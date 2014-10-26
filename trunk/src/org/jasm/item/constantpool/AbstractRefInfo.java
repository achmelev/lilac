package org.jasm.item.constantpool;

import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.MethodDescriptor;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractRefInfo extends AbstractReferenceEntry implements INameReferencingEntry, IDescriptorReferencingEntry {
	
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
		return (ClassInfo)getConstantReferences()[0];
	}
	
	public String getClassName() {
		return getClassReference().getClassName();
	}
	
	public NameAndTypeInfo getNameAndTypeReference() {
		return (NameAndTypeInfo)getConstantReferences()[1];
	}
	
	public String getName() {
		return getNameAndTypeReference().getName();
	}
	
	public String getSignature() {
		return getNameAndTypeReference().getDescriptor();
	}

	

	@Override
	public String getPrintComment() {
		return "class="+getClassName()+", name="+getName()+", descriptor="+getSignature();
	}


	@Override
	public String[] getReferencedDescriptors() {
		return new String[]{getSignature()};
	}

	@Override
	public String[] getReferencedNames() {
		return new String[]{getName(), getClassName()};
	}

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		if (index == 1) {
			NameAndTypeInfo nti = (NameAndTypeInfo)value;
			String descriptor = nti.getDescriptor();
			if (isMethodRef()) {
				try {
					MethodDescriptor desc = new MethodDescriptor(descriptor);
				} catch (IllegalDescriptorException e) {
					emitError(ref, "expected method but got type descriptor");
					return false;
				}
			} else {
				try {
					TypeDescriptor desc = new TypeDescriptor(descriptor);
				} catch (IllegalDescriptorException e) {
					emitError(ref, "expected type but got method descriptor");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new ClassInfo(),new NameAndTypeInfo()};
	}
	
	protected abstract boolean isMethodRef();

}
