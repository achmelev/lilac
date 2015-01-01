package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.item.clazz.Method;
import org.jasm.type.descriptor.MethodDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractParameterAnnotationsAttributeContent extends AbstractBytecodeItemList<ParameterAnnotations> implements IAttributeContent {
	
	private List<Annotation> annotations = new ArrayList<>();
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void prepareRead(int length) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void doResolve() {
		super.doResolve();
		Method m = (Method)getParent().getParent().getParent();
		int numberOfParameters = new MethodDescriptor(m.getDescriptor().getValue()).getParameters().size();
		if (numberOfParameters != getSize()) {
			log.warn("Parameter descriptor and param annotations number don't match: "+numberOfParameters+"!="+getSize());
		}
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
				throw new IllegalStateException("no parameter index set");
			} else if (index> getSize()-1 || index < 0) {
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
