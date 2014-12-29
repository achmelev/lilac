package org.jasm.item.constantpool;

import org.jasm.resolver.FieldInfo;
import org.jasm.type.verifier.VerifierParams;


public class FieldrefInfo extends AbstractRefInfo {
	
	private FieldInfo externalInfo = null;
	
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
	protected void doVerify(VerifierParams params) {
		externalInfo = getRoot().checkAndLoadFieldInfo(this, referenceLabels[0], getClassName(),getName(), getDescriptor(), false);
	}
	

	
	
	
	

}
