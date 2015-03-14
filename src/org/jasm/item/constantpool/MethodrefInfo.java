package org.jasm.item.constantpool;

import org.jasm.resolver.MethodInfo;

public class MethodrefInfo extends AbstractRefInfo {
	
	
	private MethodInfo externalInfo;
	
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
	
	@Override
	protected void doVerify() {
		externalInfo = getRoot().checkAndLoadMethodInfo(this, referenceLabels[0], getClassName(),getName(), getDescriptor(), false);
	}
	
	
	

}
