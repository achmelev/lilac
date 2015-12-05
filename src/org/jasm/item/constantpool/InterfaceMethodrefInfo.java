package org.jasm.item.constantpool;

import org.jasm.resolver.MethodInfo;

public class InterfaceMethodrefInfo extends AbstractRefInfo {
	

	public InterfaceMethodrefInfo() {
		super();
	}

	@Override
	public short getTag() {
		return 11;
	}

	
	@Override
	public String getConstTypeLabel() {
		return  "intfmethodref";
	}

	@Override
	protected boolean isMethodRef() {
		return true;
	}
	
	@Override
	protected void doVerify() {

	}

	@Override
	public void completeGeneratedEntry() {
		// TODO Auto-generated method stub
		
	}

}
