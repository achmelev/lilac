package org.jasm.item.constantpool;



import java.util.List;

import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class NameAndTypeInfo extends AbstractReferenceEntry implements INameReferencingEntry, IDescriptorReferencingEntry, IUtf8ConstantPoolReference  {
	
	private SymbolReference descriptorRef; 
	private TypeDescriptor typeDescriptor;
	private MethodDescriptor methodDescriptor;
	
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
	
	private Utf8Info nameToVerify;
	private SymbolReference nameSymbolToVerify;

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		String valueStr = ((Utf8Info)value).getValue();
		if (index == 0) {
			nameToVerify = (Utf8Info)value;
			nameSymbolToVerify = ref;
		} else if (index == 1) {
				descriptorRef = ref;
				try {
					methodDescriptor = new MethodDescriptor(valueStr);
				} catch (IllegalDescriptorException e) {
					try {
						typeDescriptor = new TypeDescriptor(valueStr);
					} catch (IllegalDescriptorException e1) {
						emitError(ref, "malformed type or method descriptor");
					}
				}
				if (typeDescriptor != null || methodDescriptor != null) {
					if (!isField()) {
						IdentifierUtils.checkMethodName(this, nameSymbolToVerify, nameToVerify);
					} else {
						IdentifierUtils.checkSimpleIdentifier(this, nameSymbolToVerify, nameToVerify);
						
					}
				}
		} else {
			throw new IllegalArgumentException("wrong index: "+index);
		}
		return true;
	}


	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info(),new Utf8Info()};
	}
	
	public boolean isField() {
		return (typeDescriptor != null);
	}


	@Override
	protected String doGetDisassemblerLabel() {
		List<AbstractRefInfo> refs = getConstantPool().getReferencingItems(this, AbstractRefInfo.class); 
		if (refs.size()>0) {
			AbstractRefInfo ref = refs.get(0);
			if (ref.getDisassemblerLabel() !=null) {
				return ref.getDisassemblerLabel()+"_nat";
			}
		}
		return null;
	}


	@Override
	public String generateName(Utf8Info utf8) {
		
		if (utf8 == getNameReference()) {
			return getNameReference().getValue()+"_name";
		} else if (utf8 == getDescriptorReference()) {
			return getNameReference().getValue()+"_desc";
		}
		
		return null;
		
	}


	public TypeDescriptor getTypeDescriptor() {
		return typeDescriptor;
	}


	public MethodDescriptor getMethodDescriptor() {
		return methodDescriptor;
	}


	@Override
	protected void doVerify() {
		
	}


	@Override
	public void completeGeneratedEntry() {
		String valueStr = getDescriptor();
		try {
			methodDescriptor = new MethodDescriptor(valueStr);
		} catch (IllegalDescriptorException e) {
			typeDescriptor = new TypeDescriptor(valueStr);
		}
	}
	
	
	

}
