package org.jasm.item.constantpool;

import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.TypeDescriptor;

public class ClassInfo extends AbstractReferenceEntry implements INameReferencingEntry, IUtf8ConstantPoolReference {
	
	private TypeDescriptor descriptor;
	
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
	public String getConstTypeLabel() {
		return  "classref";
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
				emitError(ref, "malformed class name or array type");
				return false;
			} else {
				descriptor = desc;
			}
		} else {
			descriptor = new TypeDescriptor("L"+className+";");
		}
		return true;
	}
	
	

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info()};
	}


	@Override
	protected String doGetDisassemblerLabel() {
		String result = getClassNameReference().getValue();
		if (result.indexOf('/')>=0) {
			result = result.substring(result.lastIndexOf('/')+1, result.length());
		} else {
			return result;
		}
		return result;
	}


	@Override
	public String generateName(Utf8Info utf8) {
		if (utf8 == getClassNameReference()) {
			return getDisassemblerLabel()+"_name";
		}
		return null;
	}
	
	
	public boolean isArray() {
		return getClassName().startsWith("[");
	}


	@Override
	protected void doVerify() {
		
	}


	public TypeDescriptor getDescriptor() {
		return descriptor;
	}


	@Override
	public void completeGeneratedEntry() {
		String className = getClassName();
		if (!IdentifierUtils.isValidJasmClassName(className)) {
			TypeDescriptor desc = null; 
			desc = new TypeDescriptor(className);
			if (!desc.isArray()) { 
				throw new IllegalStateException("malformed class name or array type");
			} else {
				descriptor = desc;
			}
		} else {
			descriptor = new TypeDescriptor("L"+className+";");
		}
	}
	
	

	
}
