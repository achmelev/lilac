package org.jasm.item.constantpool;

public class StringInfo extends AbstractReferenceEntry {
	
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
		return (Utf8Info)getReference()[0];
	}
	
	public String getContent() {
		return getUtf8Reference().getValue();
	}

}
