package org.jasm.item.attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.item.IBytecodeItem;

public class ExceptionHandlerTable extends AbstractBytecodeItemList<ExceptionHandler> {
	
	
	private Set<ExceptionHandler> referencedExceptionHandlers = new HashSet<>();
	
	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  "exception handler table";
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
	

}
