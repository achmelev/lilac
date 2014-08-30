package org.jasm.item.constantpool;

import org.jasm.parser.literals.SymbolReference;

public class FieldrefInfo extends AbstractRefInfo {
	
	
	
	public FieldrefInfo() {
		super();
	}

	public FieldrefInfo(ClassInfo clazz, NameAndTypeInfo nameAndType) {
		super(clazz, nameAndType);
	}

	@Override
	public short getTag() {
		return 9;
	}

	@Override
	public String getPrintName() {
		return "fieldrefinfo";
	}

	
	
	
	

}
