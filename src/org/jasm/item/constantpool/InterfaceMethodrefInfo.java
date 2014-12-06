package org.jasm.item.constantpool;

public class InterfaceMethodrefInfo extends AbstractRefInfo {
	
	

	public InterfaceMethodrefInfo() {
		super();
	}

	@Override
	public short getTag() {
		return 11;
	}

	
	@Override
	public String getTypeLabel() {
		return  "const intfmethodref";
	}

	@Override
	protected boolean isMethodRef() {
		return true;
	}
	
	

}
