package org.jasm.item.constantpool;

import org.jasm.resolver.MethodInfo;
import org.jasm.type.verifier.VerifierParams;

public class InterfaceMethodrefInfo extends AbstractRefInfo {
	
	private MethodInfo externalInfo;

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
	protected void doVerify(VerifierParams params) {
		externalInfo = getRoot().checkAndLoadInterfaceMethodInfo(this, referenceLabels[0], getClassName(),getName(), getDescriptor(), false);
	}

}
