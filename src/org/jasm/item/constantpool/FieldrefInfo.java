package org.jasm.item.constantpool;

import org.jasm.item.utils.IdentifierUtils;
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
	public String getTypeLabel() {
		return  "const fieldref";
	}

	

	@Override
	protected boolean isMethodRef() {
		return false;
	}

	

	
	
	
	

}
