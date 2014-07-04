package org.jasm.item.constantpool;

public class MethodrefInfo extends AbstractRefInfo {
	
	

	public MethodrefInfo() {
		super();
	}

	public MethodrefInfo(ClassInfo clazz, NameAndTypeInfo nameAndType) {
		super(clazz, nameAndType);
	}

	@Override
	public short getTag() {
		return 10;
	}

	@Override
	public String getPrintName() {
		return "methodrefinfo";
	}
	
	

}
