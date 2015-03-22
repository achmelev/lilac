package org.jasm.item.constantpool;

import org.jasm.resolver.FieldInfo;


public class FieldrefInfo extends AbstractRefInfo {
	
	
	public FieldrefInfo() {
		super();
	}

	@Override
	public short getTag() {
		return 9;
	}

	
	@Override
	public String getConstTypeLabel() {
		return  "fieldref";
	}

	

	@Override
	protected boolean isMethodRef() {
		return false;
	}
	
	@Override
	protected void doVerify() {
		
	}
	

	
	
	
	

}
