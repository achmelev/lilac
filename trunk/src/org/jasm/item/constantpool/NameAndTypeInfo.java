package org.jasm.item.constantpool;



import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.MethodDescriptor;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

public class NameAndTypeInfo extends AbstractReferenceEntry implements INameReferencingEntry, IDescriptorReferencingEntry {

	public NameAndTypeInfo() {
		
	}
	
	public NameAndTypeInfo(Utf8Info name, Utf8Info descriptor) {
		super(new Utf8Info[]{name, descriptor});
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
	public String getTypeLabel() {
		return "const nameandtype";
	}

	@Override
	public String getPrintComment() {
		return getName()+" "+getDescriptor();
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
			if (!IdentifierUtils.isValidIdentifier(valueStr) && !(valueStr.equals("<init>") || valueStr.equals("<cinit>"))) {
				emitError(ref, "malformed method or field name:  "+valueStr);
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

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info(),new Utf8Info()};
	}
	
	

}
