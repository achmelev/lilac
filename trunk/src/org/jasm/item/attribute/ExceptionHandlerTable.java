package org.jasm.item.attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SymbolTable;
import org.jasm.parser.literals.SymbolReference;

public class ExceptionHandlerTable extends AbstractBytecodeItemList<ExceptionHandler>  {
	
	
	private Set<ExceptionHandler> referencedExceptionHandlers = new HashSet<>();
	
	private SymbolTable symbolTable = new SymbolTable(null);
	
	@Override
	public String getPrintName() {
		return null;
	}

	@Override
	protected ExceptionHandler createEmptyItem(IByteBuffer source, long offset) {
		return new ExceptionHandler();
	}

	@Override
	protected void doUpdateMetadata() {
		referencedExceptionHandlers.clear();
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		List<IBytecodeItem> items = code.getAllItemsFromHere();
		for (IBytecodeItem item: items) {
			if (item instanceof IExceptionHandlerReference) {
				IExceptionHandlerReference ref = (IExceptionHandlerReference)item;
				for (ExceptionHandler handler: ref.getExceptionHandlerReferences()) {
					if (getItems().contains(handler)) {
						referencedExceptionHandlers.add(handler);
					} else {
						throw new IllegalStateException("orphan or foreign exception handler");
					}
				}
			}
		}
	}
	
	boolean isReferenced(ExceptionHandler handler) {
		return referencedExceptionHandlers.contains(handler);
	}
	
	int getHandlerIndex(ExceptionHandler handler) {
		return indexOf(handler);
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public ExceptionHandler checkAndLoadFromSymbolTable(AbstractByteCodeItem caller, SymbolReference ref) {
		ExceptionHandler result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			result =  (ExceptionHandler)entry;
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown exception handler "+ref.getSymbolName());
			}
		}
		return result;
	}
	
	
	
	
	
}
