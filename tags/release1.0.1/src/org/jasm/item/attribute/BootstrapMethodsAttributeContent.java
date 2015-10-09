package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.disassembler.NameGenerator;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SymbolTable;
import org.jasm.parser.literals.SymbolReference;

public class BootstrapMethodsAttributeContent extends
	AbstractBytecodeItemList<BootstrapMethod> implements IAttributeContent {
	
	private NameGenerator bootstrapNameGenerator = new NameGenerator();
	private SymbolTable symbolTable = new SymbolTable(null);
	
	@Override
	public String getPrintName() {
		return null;
	}
	
	
	@Override
	public void prepareRead(int length) {
	
	}
	
	@Override
	protected BootstrapMethod createEmptyItem(IByteBuffer source, long offset) {
		return new BootstrapMethod();
	}


	public NameGenerator getBootstrapNameGenerator() {
		return bootstrapNameGenerator;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
	public BootstrapMethod checkAndLoadFromSymbolTable(AbstractByteCodeItem caller, SymbolReference ref) {
		BootstrapMethod result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			return (BootstrapMethod)entry;
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown bootstrap method"+ref.getSymbolName());
			}
		}
		return result;
	}

	

}
