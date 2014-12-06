package org.jasm.item.constantpool;



import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.MethodDescriptor;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

public class NameAndTypeInfo extends AbstractReferenceEntry implements INameReferencingEntry, IDescriptorReferencingEntry {

	public NameAndTypeInfo() {
		
	}
	

	@Override
	public short getTag() {
		return 12;
	}

	@Override
	protected int getNumberOfReferences() {
		return 2;
	}
	
	public Utf8Info getNameReference() {
		return (Utf8Info)getConstantReferences()[0];
	}
	
	public String getName() {
		return  getNameReference().getValue();
	}
	
	public Utf8Info getDescriptorReference() {
		return  (Utf8Info)getConstantReferences()[1];
	}
	
	public String getDescriptor() {
		return  getDescriptorReference().getValue();
	}

	@Override
	public String getConstTypeLabel() {
		return "nameandtype";
	}

	@Override
	public String getPrintComment() {
		return "name="+getName()+" type="+getDescriptor();
	}

	@Override
	public String[] getReferencedDescriptors() {
		return new String[]{getDescriptor()};
	}

	@Override
	public String[] getReferencedNames() {
		return new String[]{getName()};
	}

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		String valueStr = ((Utf8Info)value).getValue();
		if (index == 0) {
			boolean isField = (getParent() instanceof AbstractRefInfo) && ((AbstractRefInfo)getParent()).isMethodRef();
			if (!isField) {
				IdentifierUtils.checkMethodName(this, ref, (Utf8Info)value);
			} else {
				IdentifierUtils.checkIdentifier(this, ref, (Utf8Info)value);
				
			}
		} else if (index == 1) {
			try {
				MethodDescriptor desc = new MethodDescriptor(valueStr);
			} catch (IllegalDescriptorException e) {
				try {
					TypeDescriptor desc = new TypeDescriptor(valueStr);
				} catch (IllegalDescriptorException e1) {
					emitError(ref, "malformed type or method descriptor");
				}
			}
		} else {
			throw new IllegalArgumentException("wrong index: "+index);
		}
		return true;
	}

	public boolean isFieldDescriptor() {
		try {
			TypeDescriptor desc = new TypeDescriptor(getDescriptor());
			return true;
		} catch (IllegalDescriptorException e) {
			return false;
		}
	}
	
	public boolean isMethodDescriptor() {
		try {
			MethodDescriptor desc = new MethodDescriptor(getDescriptor());
			return true;
		} catch (IllegalDescriptorException e) {
			return false;
		}
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info(),new Utf8Info()};
	}
	
	

}
