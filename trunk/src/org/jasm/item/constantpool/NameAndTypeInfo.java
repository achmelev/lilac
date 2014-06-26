package org.jasm.item.constantpool;

public class NameAndTypeInfo extends AbstractReferenceEntry {
	
	public NameAndTypeInfo() {
		
	}
	
	public NameAndTypeInfo(Utf8Info name, Utf8Info descriptor) {
		super(new Utf8Info[]{name, descriptor});
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
		return (Utf8Info)getReference()[0];
	}
	
	public String getName() {
		return  getNameReference().getValue();
	}
	
	public Utf8Info getDescriptorReference() {
		return  (Utf8Info)getReference()[1];
	}
	
	public String getDescriptor() {
		return  getDescriptorReference().getValue();
	}

	@Override
	public String getPrintName() {
		return "nameandtypeinfo";
	}

	@Override
	public String getPrintComment() {
		return getName()+" "+getDescriptor();
	}

}
