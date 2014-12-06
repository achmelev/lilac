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
	public String getConstTypeLabel() {
		return "methodref";
	}

	@Override
	protected boolean isMethodRef() {
		return true;
	}
	
	
	

}
