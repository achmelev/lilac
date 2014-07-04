package org.jasm.item.constantpool;

public class InterfaceMethodrefInfo extends AbstractRefInfo {
	
	

	public InterfaceMethodrefInfo() {
		super();
	}

	public InterfaceMethodrefInfo(ClassInfo clazz, NameAndTypeInfo nameAndType) {
		super(clazz, nameAndType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public short getTag() {
		return 11;
	}

	@Override
	public String getPrintName() {
		return "interfacemethodref";
	}
	
	

}
