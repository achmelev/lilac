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
	public String getPrintName() {
		return "fieldrefinfo";
	}

	@Override
	protected boolean verifyReference(int index, SymbolReference ref,
			AbstractConstantPoolEntry value) {
		if (index == 1) {
			NameAndTypeInfo nti = (NameAndTypeInfo)value;
			if (!IdentifierUtils.isValidIdentifier(nti.getName())) {
				emitError(ref, "illegal field name: "+nti.getName());
				return false;
			}
		}
		
		return true;
	}

	

	
	
	
	

}
