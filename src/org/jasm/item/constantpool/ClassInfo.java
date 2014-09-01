package org.jasm.item.constantpool;

import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;

public class ClassInfo extends AbstractReferenceEntry implements INameReferencingEntry {
	
	public ClassInfo() {
	}
	
	public ClassInfo(Utf8Info name) {
		super(new Utf8Info[]{name} );
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
			emitError(ref, "invalid class name "+className);
			return false;
		}
		return true;
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info()};
	}
	
	
	
	
	
	

}
