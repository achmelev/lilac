package org.jasm.item.constantpool;

public class InterfaceMethodrefInfo extends AbstractRefInfo {

	@Override
	public short getTag() {
		return 11;
	}

	@Override
	public String getPrintName() {
		return "interfacemethodref";
	}
	
	

}
