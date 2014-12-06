package org.jasm.item.constantpool;

import org.jasm.parser.literals.SymbolReference;

public class StringInfo extends AbstractReferenceEntry implements ITextReferencingEntry {
	
	public StringInfo() {
		
	}
	

	@Override
	public short getTag() {
		return 8;
	}

	@Override
	protected int getNumberOfReferences() {
		return 1;
	}
	
	public Utf8Info getUtf8Reference() {
		return (Utf8Info)getConstantReferences()[0];
	}
	
	public String getContent() {
		return getUtf8Reference().getValue();
	}
	
	@Override
	public String getTypeLabel() {
		return "const string";
	}

	@Override
	public String getPrintComment() {
		return getUtf8Reference().getPrintArgs();
	}

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		return true;
	}

	@Override
	protected AbstractConstantPoolEntry[] getExpectedReferenceTypes() {
		return new AbstractConstantPoolEntry[]{new Utf8Info()};
	}
	
	

}
