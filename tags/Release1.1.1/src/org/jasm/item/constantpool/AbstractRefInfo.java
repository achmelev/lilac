package org.jasm.item.constantpool;

import org.jasm.disassembler.ClassNameGenerator;
import org.jasm.disassembler.NameGenerator;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractRefInfo extends AbstractReferenceEntry implements INameReferencingEntry, IDescriptorReferencingEntry {
	
	public AbstractRefInfo() {
		
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
	
	public String getDescriptor() {
		return getNameAndTypeReference().getDescriptor();
	}

	

	@Override
	public String getPrintComment() {
		return "class="+getClassName()+", name="+getName()+", descriptor="+getDescriptor();
	}


	@Override
	public String[] getReferencedDescriptors() {
		return new String[]{getDescriptor()};
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
			if (isMethodRef()) {
				if (nti.isField()) {
					emitError(ref, "wrong nameandtype const");
					return false;
				}
			} else {
				if (!nti.isField()) {
					emitError(ref, "wrong nameandtype const");
					return false;
				}
			}
		} else if (index == 0 && !isMethodRef()) { //despite jvm spec, it's allowed to habe method refs to arrays (e.g clone)
			ClassInfo cli = (ClassInfo)value;
			if (cli.getDescriptor().isArray()) {
				emitError(ref, "malformed class or interface name");
				return false;
			}
		}
		return true;
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new ClassInfo(),new NameAndTypeInfo()};
	}
	
	protected abstract boolean isMethodRef();

	

	@Override
	protected String doGetDisassemblerLabel() {
		String className = getClassReference().getDisassemblerLabel();
		String result =  getNameAndTypeReference().getNameReference().getValue();
		if (!className.equals(ClassNameGenerator.THISCLASS) || NameGenerator.isKeyword(result)) {
			result = className+"."+result;
		}
		return result;
	}
	
	

}
