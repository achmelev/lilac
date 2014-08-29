package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class AnnotationDefaultAttributeContent extends
		AbstractSimpleAttributeContent implements IContainerBytecodeItem<AnnotationElementValue>{
	
	private AnnotationElementValue value = null;
	
	public AnnotationDefaultAttributeContent() {
		
	}
	
	public AnnotationDefaultAttributeContent(AnnotationElementValue value) {
		this.value = value;
		this.value.setParent(this);
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		value = new AnnotationElementValue();
		value.setParent(this);
		value.read(source, offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		value.write(target, offset);
	}

	@Override
	public int getLength() {
		return value.getLength();
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		result.add(value);
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "annotation default";
	}

	@Override
	public String getPrintArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintComment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doResolve() {
		value.resolve();

	}
	
	@Override
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public AnnotationElementValue get(int index) {
		if (index == 0) {
			return value;
		} else {
			throw new IndexOutOfBoundsException(index+"");
		}
		
	}

	@Override
	public int indexOf(AnnotationElementValue item) {
		if (item == value) {
			return 0;
		} else {
			return -1;
		}
		
	}

	public AnnotationElementValue getValue() {
		return value;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	
	

}
