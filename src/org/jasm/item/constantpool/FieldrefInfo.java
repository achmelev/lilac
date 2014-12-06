package org.jasm.item.constantpool;

import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.SymbolReference;




public class FieldrefInfo extends AbstractRefInfo {
	
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

	

	
	
	
	

}
