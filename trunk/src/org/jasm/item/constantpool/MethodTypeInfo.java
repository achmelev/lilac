package org.jasm.item.constantpool;

public class MethodTypeInfo extends AbstractReferenceEntry {
	
	public MethodTypeInfo() {
		
	}
	
	public MethodTypeInfo(Utf8Info descriptor) {
		super(new Utf8Info[]{descriptor});
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
		return (Utf8Info)getReference()[0];
	}
	
	public String getDescriptor() {
		return getDescriptorReference().getValue();
	}

}
