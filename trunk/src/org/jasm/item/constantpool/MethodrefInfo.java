package org.jasm.item.constantpool;

import org.jasm.resolver.MethodInfo;
import org.jasm.type.verifier.VerifierParams;

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
	protected void doVerify(VerifierParams params) {
		externalInfo = getRoot().checkAndLoadMethodInfo(this, referenceLabels[0], getClassName(),getName(), getDescriptor(), false);
	}
	
	
	

}
