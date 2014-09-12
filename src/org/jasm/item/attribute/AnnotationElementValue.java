package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ElementVisitor;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnnotationElementValue extends AbstractByteCodeItem implements IContainerBytecodeItem<IBytecodeItem>, IConstantPoolReference {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private char tag = 0;
    
    private int primitiveValueIndex = -1;
    private AbstractConstantPoolEntry primitiveValueEntry = null;
    
    private int enumTypeNameIndex = -1;
    private Utf8Info enumTypeName = null;
    private int enumConstNameIndex = -1;
    private Utf8Info enumConstName = null;
    
    private int classInfoIndex = -1;
    private Utf8Info classInfo = null;
    
    private Annotation nestedAnnotation = null;
    
    private AnnotationElementValue[] arrayMembers = null;
    
    public AnnotationElementValue() {
    	
    }
    
    public AnnotationElementValue(IntegerInfo value, char descriptor) {
    	if (descriptor=='B' || descriptor=='C' || descriptor=='I' || descriptor=='S' || descriptor=='Z') {
    		this.primitiveValueEntry= value;
    		tag = descriptor;
    	} else {
    		throw new IllegalArgumentException(" Illegal descriptor: "+descriptor);
    	}
    }
    
    public AnnotationElementValue(StringInfo value) {
    	tag = 's';
    	this.primitiveValueEntry = value;
    }
    
    public AnnotationElementValue(Utf8Info enumConstName, Utf8Info enumTypeName) {
    	this.enumConstName = enumConstName;
    	this.enumTypeName = enumTypeName;
    	this.tag = 'e';
    }
    
    public AnnotationElementValue(Utf8Info classInfo) {
    	this.classInfo = classInfo;
    	this.tag = 'c';
    }
    
    public AnnotationElementValue(Annotation nestedValue) {
    	this.nestedAnnotation = nestedValue;
    	this.tag = '@';
    	nestedValue.setParent(this);
    }
    
    public AnnotationElementValue(AnnotationElementValue[] arrayMembers) {
    	this.arrayMembers = arrayMembers;
    	this.tag = '[';
    	for (AnnotationElementValue arrayMember: arrayMembers) {
    		arrayMember.setParent(this);
    	}
    }

	@Override
	public void read(IByteBuffer source, long offset) {
		tag = (char)source.readUnsignedByte(offset);
		
		
		if (isPrimitiveValue()) {
			primitiveValueIndex = source.readUnsignedShort(offset+1);
		} else if (isEnumValue()) {
			enumTypeNameIndex = source.readUnsignedShort(offset+1);
			enumConstNameIndex = source.readUnsignedShort(offset+3);
		} else if (isClassValue()) {
			classInfoIndex = source.readUnsignedShort(offset+1);
		} else if (isNested()) {
			nestedAnnotation = new Annotation();
			nestedAnnotation.setParent(this);
			if (log.isDebugEnabled()) {
				log.debug("read nested annotation  offset="+(offset+1));
			}
			nestedAnnotation.read(source, offset+1);
		} else if (isArray()) {
			int arraySize = source.readUnsignedShort(offset+1);
			long currentOffset = offset+3;
			arrayMembers = new AnnotationElementValue[arraySize];
			for (int i=0;i<arraySize; i++) {
				arrayMembers[i] = new AnnotationElementValue();
				arrayMembers[i].setParent(this);
				if (log.isDebugEnabled()) {
					log.debug("read annotation array member "+i+"/"+arraySize+" offset="+currentOffset);
				}
				arrayMembers[i].read(source, currentOffset);
				currentOffset+=arrayMembers[i].getLength();
			}
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
		
	}
	

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, (short)tag);
		if (isPrimitiveValue()) {
			target.writeUnsignedShort(offset+1, primitiveValueEntry.getIndexInPool());
		} else if (isEnumValue()) {
			target.writeUnsignedShort(offset+1, enumTypeName.getIndexInPool());
			target.writeUnsignedShort(offset+3, enumConstName.getIndexInPool());
		} else if (isClassValue()) {
			target.writeUnsignedShort(offset+1, classInfo.getIndexInPool());
		} else if (isNested()) {
			nestedAnnotation.write(target, offset+1);
		} else if (isArray()) {
			target.writeUnsignedShort(offset+1, arrayMembers.length);
			long currentOffset = offset+3;
			for (int i=0;i<arrayMembers.length; i++) {
				if (log.isDebugEnabled()) {
					log.debug("write annotation array member "+i+"/"+arrayMembers.length+" offset="+currentOffset);
				}
				arrayMembers[i].write(target, currentOffset);
				currentOffset+=arrayMembers[i].getLength();
			}
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
		
	}

	@Override
	public int getLength() {
		if (isPrimitiveValue()) {
			return 3;
		} else if (isEnumValue()) {
			return 5;
		} else if (isClassValue()) {
			return 3;
		} else if (isNested()) {
			return 1+nestedAnnotation.getLength();
		} else if (isArray()) {
			int size = 3;
			for (int i=0;i<arrayMembers.length; i++) {
				size+=arrayMembers[i].getLength();
			}
			return size;
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
	}

	@Override
	public boolean isStructure() {
		return isNested() || isArray();
	}

	@Override
	public List<IPrintable> getStructureParts() {
		if (isNested()) {
			List<IPrintable> result = new ArrayList<IPrintable>();
			result.add(nestedAnnotation);
			return result;
		} else if  (isArray()){
			List<IPrintable> result = new ArrayList<IPrintable>();
			for (IPrintable m: arrayMembers) {
				result.add(m);
			}
			return result;
		} else {
			return null;
		}
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return getPrintPrefix()+" value";
	}
	
	
	
	@Override
	public String getTypeLabel() {
		return  "annotation value";
	}

	@Override
	public String getPrintArgs() {
		if (isPrimitiveValue()) {
			return primitiveValueEntry.getSymbolName();
		} else if (isEnumValue()) {
			return enumConstName.getSymbolName()+", "+enumTypeName.getSymbolName();
		} else if (isClassValue()) {
			return classInfo.getSymbolName();
		} else if (isNested() || isArray()) {
			return null;
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
	}

	@Override
	public String getPrintComment() {
		if (isPrimitiveValue()) {
			if (primitiveValueEntry instanceof IPrimitiveValueReferencingEntry) {
				return ((IPrimitiveValueReferencingEntry)primitiveValueEntry).getValue().toString();
			} else if (primitiveValueEntry instanceof StringInfo) {
				return ((StringInfo)primitiveValueEntry).getContent();
			} else if (primitiveValueEntry instanceof Utf8Info) {
				return ((Utf8Info)primitiveValueEntry).getValue();
			} else {
				throw new IllegalStateException("Wrong entry: "+primitiveValueEntry);
			}
		} else if (isEnumValue()) {
			return enumConstName.getValue()+", "+enumTypeName.getValue();
		} else if (isClassValue()) {
			return classInfo.getValue();
		} else if (isNested() || isArray()) {
			return null;
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
	}

	@Override
	protected void doResolve() {
		if (isPrimitiveValue()) {
			primitiveValueEntry = getConstantPool().get(primitiveValueIndex-1);
		} else if (isEnumValue()) {
			enumConstName = (Utf8Info)getConstantPool().get(enumConstNameIndex-1);
			enumTypeName = (Utf8Info)getConstantPool().get(enumTypeNameIndex-1);
		} else if (isClassValue()) {
			classInfo = (Utf8Info)getConstantPool().get(classInfoIndex-1);
		} else if (isNested()) {
			nestedAnnotation.resolve();
		} else if (isArray()) {
			for (AnnotationElementValue m: arrayMembers) {
				m.resolve();
			}
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
		
	}
	
	@Override
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}

	public boolean isPrimitiveValue() {
		return (tag == 'B' || 
				tag == 'C' || 
				tag == 'D' ||
				tag == 'F' ||
				tag == 'I' ||
				tag == 'J' ||
				tag == 'S' ||
				tag == 'Z' ||
				tag == 's');
	}
	
	public boolean isEnumValue() {
		return (tag == 'e');
	}
	
	public boolean isClassValue() {
		return (tag == 'c');
	}
	
	public boolean isNested() {
		return (tag == '@');
	}
	
	public boolean isArray() {
		return (tag == '[');
	}
	
	private String getPrintPrefix() {
		String result = null;
		switch (tag) {
			case 'B': result = "byte";break;
			case 'C': result = "char";break;
			case 'D': result = "double";break;
			case 'F': result = "float";break;
			case 'I': result = "int";break;
			case 'J': result = "long";break;
			case 'S': result = "short";break;
			case 'Z': result = "boolean";break;
			case 'e': result = "enum";break;
			case 'c': result = "class";break;
			case '@': result = "nested";break;
			case '[': result = "array";break;
			default: throw new IllegalStateException("unknown tag: "+tag);
		}
		return result;
	}

	@Override
	public int getSize() {
		if (isNested()) {
			return 1;
		} else if (isArray()) {
			return arrayMembers.length;
		}
		return 0;
	}

	@Override
	public IBytecodeItem get(int index) {
		if (isArray()) {
			return arrayMembers[index];
		} else if (isNested()) {
			if (index > 0) {
				throw new IndexOutOfBoundsException();
			} else  {
				return nestedAnnotation;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
		
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		if (isArray()) {
			for (int i=0;i<arrayMembers.length; i++) {
				if (arrayMembers[i] == item) {
					return i;
				} else {
					return -1;
				}
			}
			return -1;
		} else if (isNested()) {
			if (this.nestedAnnotation == item) {
				return 0;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
		
	}

	public AbstractConstantPoolEntry getPrimitiveValueEntry() {
		return primitiveValueEntry;
	}
	
	public Object getPrimitiveValue() {
		if (primitiveValueEntry instanceof IPrimitiveValueReferencingEntry) {
			return ((IPrimitiveValueReferencingEntry)primitiveValueEntry).getValue();
		} else if (primitiveValueEntry instanceof StringInfo) {
			return ((StringInfo)primitiveValueEntry).getContent();
		} else if (primitiveValueEntry instanceof Utf8Info) {
			return ((Utf8Info)primitiveValueEntry).getValue();
		} else {
			throw new IllegalStateException("Wrong entry: "+primitiveValueEntry);
		}
	}

	public Utf8Info getEnumTypeName() {
		return enumTypeName;
	}
	
	public String getEnumTypeNameValue() {
		return enumTypeName.getValue();
	}

	public Utf8Info getEnumConstName() {
		return enumConstName;
	}
	
	public String getEnumConstNameValue() {
		return enumConstName.getValue();
	}

	public Utf8Info getClassInfo() {
		return classInfo;
	}
	
	public String getClassName() {
		return classInfo.getValue();
	}

	public Annotation getNestedAnnotation() {
		return nestedAnnotation;
	}

	public AnnotationElementValue[] getArrayMembers() {
		return arrayMembers;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		if (isPrimitiveValue()) {
			return new AbstractConstantPoolEntry[]{primitiveValueEntry};
		} else if (isEnumValue()) {
			return new AbstractConstantPoolEntry[]{enumTypeName, enumConstName};
		} else if (isClassValue()) {
			return new AbstractConstantPoolEntry[]{classInfo};
		} else {
			return new AbstractConstantPoolEntry[]{};
		}
	}
	
	
}
