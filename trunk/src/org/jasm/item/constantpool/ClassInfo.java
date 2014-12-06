package org.jasm.item.constantpool;

import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

public class ClassInfo extends AbstractReferenceEntry implements INameReferencingEntry {
	
	public ClassInfo() {
	}
	
	
	@Override
	protected int getNumberOfReferences() {
		return 1;
	}

	@Override
	public short getTag() {
		return 7;
	}
	
	public Utf8Info getClassNameReference() {
		return  (Utf8Info)getConstantReferences()[0];
	}
	
	public String getClassName() {
		return getClassNameReference().getValue();
	}

	
	@Override
	public String getTypeLabel() {
		return  "const classref";
	}

	@Override
	public String getPrintComment() {
		return getClassName();
	}

	@Override
	public String[] getReferencedNames() {
		return new String[]{getClassName()};
	}

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		String className = ((Utf8Info)value).getValue();
		if (!IdentifierUtils.isValidJasmClassName(className)) {
			TypeDescriptor desc = null;
			try {
				desc = new TypeDescriptor(className);
			} catch (IllegalDescriptorException e) {
				
			}
			if (desc == null || !desc.isArray()) { 
				emitError(ref, "invalid class name or array type"+className);
				return false;
			}
		}
		return true;
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info()};
	}
	
	
	
	
	
	

}
