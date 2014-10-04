package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.descriptor.IllegalDescriptorException;
import org.jasm.item.descriptor.TypeDescriptor;
import org.jasm.parser.literals.SymbolReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Annotation extends AbstractByteCodeItem implements IContainerBytecodeItem<AnnotationElementNameValue>, IConstantPoolReference {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private boolean parameterIndexSet= false;
	private int parameterIndex = -1;
	
	private int typeIndex = -1;
	private SymbolReference typeValueReference;
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
		result.add(new SimplePrintable(null, "type", type.getSymbolName(), type.getValue()));
		if (getParent() instanceof ParameterAnnotations) {
			AbstractParameterAnnotationsAttributeContent greatParent = (AbstractParameterAnnotationsAttributeContent)this.getParent().getParent();
			int index = greatParent.indexOf((ParameterAnnotations)getParent());
			result.add(new SimplePrintable(null, "index", index+"", null));
		}
		result.addAll(values);
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		StringBuffer buf = new StringBuffer();
		if ((this.getParent() instanceof RuntimeInvisibleAnnotationsAttributeContent) || getParent().getParent() instanceof RuntimeInvisibleParameterAnnotationsAttributeContent) {
			buf.append("invisible ");
		} 
		if (this.getParent() instanceof ParameterAnnotations) {
			buf.append("parameter ");
		}
		buf.append("annotation");
		return buf.toString();
	}
	
	

	@Override
	public String getTypeLabel() {
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
	protected void doResolveAfterParse() {
		type = getConstantPool().checkAndLoadFromSymbolTable(Utf8Info.class, typeValueReference);
		if (type != null) {
			if (verifyDescriptor(typeValueReference, type.getValue())) {
				for (AnnotationElementNameValue value: values) {
					value.resolve();
				}
			}
		}
	}
	
	private  boolean verifyDescriptor(SymbolReference ref, String descriptor) {
		try {
			TypeDescriptor d = new TypeDescriptor(descriptor);
		} catch (IllegalDescriptorException e) {
			emitError(ref, "malformed type descriptor "+descriptor);
			return false;
		}
		
		return true;
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

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{type};
	}

	public void setTypeValueReference(SymbolReference typeValueReference) {
		this.typeValueReference = typeValueReference;
	}
	
	public void addElement(AnnotationElementNameValue element) {
		element.setParent(this);
		values.add(element);
	}

	public void setParameterIndex(int parameterIndex) {
		this.parameterIndexSet = true;
		this.parameterIndex = parameterIndex;
	}

	public int getParameterIndex() {
		return parameterIndex;
	}

	public boolean isParameterIndexSet() {
		return parameterIndexSet;
	}
	
	
	
	
}
