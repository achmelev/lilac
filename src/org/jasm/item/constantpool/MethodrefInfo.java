package org.jasm.item.constantpool;

public class MethodrefInfo extends AbstractRefInfo {
	
	

	public MethodrefInfo() {
		super();
	}

	@Override
	public short getTag() {
		return 10;
	}

	
	@Override
	public String getTypeLabel() {
		return "const methodref";
	}

	@Override
	protected boolean isMethodRef() {
		return true;
	}
	
	
	

}
