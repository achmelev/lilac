package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.TypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Annotation extends AbstractByteCodeItem implements IContainerBytecodeItem<AbstractByteCodeItem>, IConstantPoolReference {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private boolean parameterIndexSet= false;
	private int parameterIndex = -1;
	
	private boolean isTypeAnnotation = false;
	private AbstractAnnotationTargetType target = null;
	private AnnotationTargetTypePath targetPath = null;
	
	private int typeIndex = -1;
	private SymbolReference typeValueReference;
	private TypeDescriptor typeDescriptor;
	private Utf8Info type = null;
	private int numberOfValues = -1;
	private List<AnnotationElementNameValue> values = new ArrayList<>();
	
	
	

	@Override
	public void read(IByteBuffer source, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("read annotation, offset="+offset);
		}
		long currentOffset = offset;
		if (isTypeAnnotation) {
			short targetType = source.readUnsignedByte(currentOffset);
			target = createAnnotationTarget(targetType);
			target.setParent(this);
			target.read(source, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("read target "+this.target+" at offset "+currentOffset);
			}
			currentOffset+=target.getLength();
			
			targetPath = new AnnotationTargetTypePath();
			targetPath.setParent(this);
			targetPath.read(source, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("read target path at offset "+currentOffset);
			}
			currentOffset+=targetPath.getLength();
		}
		
		typeIndex = source.readUnsignedShort(currentOffset);
		numberOfValues = source.readUnsignedShort(currentOffset+2);
		currentOffset = currentOffset+4;
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
		long currentOffset=offset;
		
		if (isTypeAnnotation) {
			this.target.write(target, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("wrote target "+this.target+" at offset "+currentOffset);
			}
			currentOffset+=this.target.getLength();
			
			targetPath.write(target, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("wrote target path at offset "+currentOffset);
			}
			currentOffset+=targetPath.getLength();
		}
		
		target.writeUnsignedShort(currentOffset, type.getIndexInPool());
		target.writeUnsignedShort(currentOffset+2, values.size());
		currentOffset += 4;
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
		if (isTypeAnnotation) {
			result+=target.getLength()+targetPath.getLength();
		}
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
		if (isTypeAnnotation) {
			result.add(target);
			result.add(targetPath);
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
		if ((this.getParent() instanceof RuntimeInvisibleAnnotationsAttributeContent) 
			|| getParent().getParent() instanceof RuntimeInvisibleParameterAnnotationsAttributeContent
			|| getParent() instanceof RuntimeInvisibleTypeAnnotationsAttributeContent) {
			buf.append("invisible ");
		} 
		if (this.getParent() instanceof ParameterAnnotations) {
			buf.append("parameter ");
		}
		if (isTypeAnnotation) {
			buf.append("type ");
		}
		buf.append("annotation");
		return buf.toString();
	}
	

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		type = (Utf8Info)getConstantPool().get(typeIndex-1);
		if (isTypeAnnotation) {
			target.resolve();
			targetPath.resolve();
		}
		for (AnnotationElementNameValue value: values) {
			value.resolve();
		}

	}
	
	
	
	@Override
	protected void doVerify() {
		getRoot().checkAndLoadTypeDescriptor(this, typeValueReference, typeDescriptor);
		if (isTypeAnnotation) {
			target.verify();
			targetPath.verify();
		}
		for (AnnotationElementNameValue value: values) {
			value.verify();
		}
		
	}

	@Override
	protected void doResolveAfterParse() {
		type = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, typeValueReference);
		if (type != null) {
			if (verifyDescriptor(typeValueReference, type.getValue())) {
				if (isTypeAnnotation) {
					target.resolve();
					if (targetPath == null) {
						targetPath = new AnnotationTargetTypePath(new short[]{}, new short[]{});
						targetPath.setParent(this);
					}
					targetPath.resolve();
				}
				for (AnnotationElementNameValue value: values) {
					value.resolve();
				}
			}
		}
	}
	
	private  boolean verifyDescriptor(SymbolReference ref, String descriptor) {
		try {
			typeDescriptor = new TypeDescriptor(descriptor);
		} catch (IllegalDescriptorException e) {
			emitError(ref, "malformed type descriptor "+descriptor);
			return false;
		}
		
		return true;
	}
	
	private AbstractAnnotationTargetType createAnnotationTarget(short targetType) {
		if (targetType == JasmConsts.ANNOTATION_TARGET_FIELD) {
			return new EmptyAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_RETURN_TYPE) {
			return new EmptyAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_RECEIVER_TYPE) {
			return new EmptyAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER) {
			return new TypeParameterAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER) {
			return new TypeParameterAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER_BOUND) {
			return new TypeParameterBoundAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER_BOUND) {
			return new TypeParameterBoundAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_SUPERTYPE) {
			return new SupertypeAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_FORMAL_PARAMETER) {
			return new FormalParameterAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_THROWS) {
			return new ThrowsAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_CAST) {
			return new TypeArgumentAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT) {
			return new TypeArgumentAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT_IN_METHOD_REF) {
			return new TypeArgumentAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT) {
			return new TypeArgumentAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF) {
			return new TypeArgumentAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_INSTANCEOF) {
			return new OffsetAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_NEW) {
			return new OffsetAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_METHOD_REF_ID) {
			return new OffsetAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_METHOD_REF_NEW) {
			return new OffsetAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_CATCH) {
			return new CatchAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_LOCAL_VAR) {
			return new LocalVariableAnnotationTargetType();
		} else if (targetType == JasmConsts.ANNOTATION_TARGET_RESOURCE_VAR) {
			return new LocalVariableAnnotationTargetType();
		} else {
			throw new IllegalArgumentException("unknown target type: "+Integer.toHexString(targetType));
		}
	}

	@Override
	public int getSize() {
		if (isTypeAnnotation) {
			return 2+values.size();
		} else {
			return values.size();
		}
		
	}

	@Override
	public AbstractByteCodeItem get(int index) {
		if (isTypeAnnotation) {
			if (index == 0) {
				return target;
			} else if (index == 1) {
				return targetPath;
			} else {
				return values.get(index-2);
			}
		} else {
			return values.get(index);
		}
	}

	@Override
	public int indexOf(AbstractByteCodeItem item) {
		if (isTypeAnnotation) {
			if (item instanceof AbstractAnnotationTargetType) {
				return (target == item)?0:-1;
			} else if (item instanceof AnnotationTargetTypePath) {
					return (targetPath == item)?1:-1;
			} else {
				return values.indexOf(item);
			}
		} else {
			return values.indexOf(item);
		}
		
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

	public boolean isTypeAnnotation() {
		return isTypeAnnotation;
	}

	public void setTypeAnnotation(boolean isTypedAnnotation) {
		this.isTypeAnnotation = isTypedAnnotation;
	}
	
	public boolean isNested() {
		return getParent() instanceof AnnotationElementValue;
	}

	public void setTargetPath(AnnotationTargetTypePath targetPath) {
		this.targetPath = targetPath;
		this.targetPath.setParent(this);
	}
	
	public AnnotationTargetTypePath getTargetPath() {
		return targetPath;
	}

	public AbstractAnnotationTargetType getTarget() {
		return target;
	}

	public void setTarget(AbstractAnnotationTargetType target) {
		this.target = target;
		this.target.setParent(this);
	}

	public TypeDescriptor getTypeDescriptor() {
		return typeDescriptor;
	}

	
	
	
	
}
