package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.item.clazz.Method;
import org.jasm.parser.SourceLocation;


public abstract class AbstractParameterAnnotationsAttributeContent extends AbstractBytecodeItemList<ParameterAnnotations> implements IAttributeContent {
	
	private List<Annotation> annotations = new ArrayList<>();
	
	@Override
	public void prepareRead(int length) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ParameterAnnotations createEmptyItem(IByteBuffer source, long offset) {
		return new ParameterAnnotations();
	}

	@Override
	protected int sizeFieldLength() {
		return 1;
	}

	@Override
	protected void doResolveAfterParse() {
		
		createParameterAnnotations();
		for (Annotation annot: annotations) {
			int index = annot.getParameterIndex();
			if (!annot.isParameterIndexSet()) {
				emitErrorOnLocation(annot.getSourceLocation(), "no index declared");
			} else if (index> getSize()-1 || index < 0) {
				SourceLocation loc = annot.getSourceLocation();
				emitErrorOnLocation(annot.getSourceLocation(), "index out of bounds");
			} else {
				getItems().get(index).add(annot);
			}
		}
		
		super.doResolveAfterParse();
	}
	
	private void createParameterAnnotations() {
		Method m = (Method)getParent().getParent().getParent();
		int numberOfParameters = m.getMethodDescriptor().getParameters().size();
		for (int i=0;i<numberOfParameters; i++) {
			add(new ParameterAnnotations());
		}
		
	}
	
	
	public void addAnnotation(Annotation annot) {
		annotations.add(annot);
	}
	
	
	
}
