package org.jasm.item.constantpool;

import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.MethodDescriptor;

public class MethodTypeInfo extends AbstractReferenceEntry implements IDescriptorReferencingEntry {
	
	
	private MethodDescriptor methodDescriptor;
	private SymbolReference methodSymbol;
	
	public MethodTypeInfo() {
		
	}
	

	@Override
	public short getTag() {
		return 16;
	}

	@Override
	protected int getNumberOfReferences() {
		return 1;
	}
	
	public Utf8Info getDescriptorReference() {
		return (Utf8Info)getConstantReferences()[0];
	}
	
	public String getDescriptor() {
		return getDescriptorReference().getValue();
	}

	
	@Override
	public String getConstTypeLabel() {
		return "methodtype";
	}

	@Override
	public String getPrintComment() {
		return getDescriptor();
	}

	@Override
	public String[] getReferencedDescriptors() {
		return new String[]{getDescriptor()};
	}
	
	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		if (index == 0) {
			String valueStr = ((Utf8Info)value).getValue();
			methodSymbol = ref;
			try {
				methodDescriptor = new MethodDescriptor(valueStr);
				return true;
			} catch (IllegalDescriptorException e) {
				emitError(ref, "malformed method descriptor");
				return false;
			}
		} else {
			throw new IllegalArgumentException("wrong index: "+index);
		}
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info()};
	}


	@Override
	protected String doGetDisassemblerLabel() {
		return null;
	}


	@Override
	protected void doVerify() {
		getRoot().checkAndLoadMethodDescriptor(this, methodSymbol, methodDescriptor);
	}
	
	

}
