package org.jasm.item.constantpool;

import org.jasm.parser.literals.SymbolReference;

public class StringInfo extends AbstractReferenceEntry implements ITextReferencingEntry {
	
	public StringInfo() {
		
	}
	
	public StringInfo(Utf8Info ref) {
		super(new Utf8Info[]{ref});
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
	public String getPrintName() {
		return "stringinfo";
	}

	@Override
	public String getPrintComment() {
		return null;
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
