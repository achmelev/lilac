package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.Utf8Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Annotation extends AbstractByteCodeItem implements IContainerBytecodeItem<AnnotationElementNameValue>{
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private int typeIndex = -1;
	private Utf8Info type = null;
	private int numberOfValues = -1;
	private List<AnnotationElementNameValue> values = new ArrayList<>();

	@Override
	public void read(IByteBuffer source, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("read annotation, offset="+offset);
		}
		typeIndex = source.readUnsignedShort(offset);
		numberOfValues = source.readUnsignedShort(offset+2);
		long currentOffset = offset+4;
		for (int i=0;i<numberOfValues; i++) {
			AnnotationElementNameValue value = new AnnotationElementNameValue();
			if (log.isDebugEnabled()) {
				log.debug("read annotation name/value "+i+"/"+numberOfValues+" offset="+currentOffset);
			}
			value.setParent(this);
			value.read(source, currentOffset);
			values.add(value);
			currentOffset+=value.getLength();
		}

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, type.getIndexInPool());
		target.writeUnsignedShort(offset+2, values.size());
		long currentOffset = offset+4;
		int counter = 0;
		for (AnnotationElementNameValue value: values) {
			if (log.isDebugEnabled()) {
				log.debug("write annotation name/value "+counter+"/"+values.size()+" offset="+currentOffset);
				counter++;
			}
			value.write(target, currentOffset);
			currentOffset+=value.getLength();
			
		}

	}

	@Override
	public int getLength() {
		int result = 4;
		for (AnnotationElementNameValue value: values) {
			result+=value.getLength();
		}
		return result;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		result.add(new SimplePrintable(null, "type", type.getPrintLabel(), type.getValue()));
		result.addAll(values);
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "annotation";
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
		type = (Utf8Info)getConstantPool().get(typeIndex-1);
		for (AnnotationElementNameValue value: values) {
			value.resolve();
		}

	}

	@Override
	public int getSize() {
		return values.size();
	}

	@Override
	public AnnotationElementNameValue get(int index) {
		return values.get(index);
	}

	@Override
	public int indexOf(AnnotationElementNameValue item) {
		return values.indexOf(item);
	}

	public Utf8Info getType() {
		return type;
	}
	
	public String getTypeValue() {
		return type.getValue();
	}

	public List<AnnotationElementNameValue> getValues() {
		return values;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	
	
	
}
