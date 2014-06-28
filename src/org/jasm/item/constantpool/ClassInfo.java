package org.jasm.item.constantpool;

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
		return  (Utf8Info)getReference()[0];
	}
	
	public String getClassName() {
		return getClassNameReference().getValue();
	}

	@Override
	public String getPrintName() {
		return "classinfo";
	}

	@Override
	public String getPrintComment() {
		return getClassName();
	}

	@Override
	public String[] getReferencedNames() {
		return new String[]{getClassName()};
	}
	
	
	

}