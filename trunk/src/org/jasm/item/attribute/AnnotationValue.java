package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;

public class AnnotationValue extends AbstractByteCodeItem {
	
	private char tag = 0;
    
    private int primitiveValueIndex = -1;
    private AbstractConstantPoolEntry primitiveValueEntry = null;
    
    private int enumTypeNameIndex = -1;
    private Utf8Info enumTypeName = null;
    private int enumConstNameIndex = -1;
    private Utf8Info enumConstName = null;
    
    private int classInfoIndex = -1;
    private Utf8Info classInfo = null;
    
    private AnnotationValue nestedValue = null;
    
    public AnnotationValue() {
    	
    }
    
    public AnnotationValue(IntegerInfo value, char descriptor) {
    	if (descriptor=='B' || descriptor=='C' || descriptor=='I' || descriptor=='S' || descriptor=='Z') {
    		this.primitiveValueEntry= value;
    		tag = descriptor;
    	} else {
    		throw new IllegalArgumentException(" Illegal descriptor: "+descriptor);
    	}
    }
    
    public AnnotationValue(StringInfo value) {
    	tag = 's';
    	this.primitiveValueEntry = value;
    }
    
    public AnnotationValue(Utf8Info enumConstName, Utf8Info enumTypeName) {
    	this.enumConstName = enumConstName;
    	this.enumTypeName = enumTypeName;
    	this.tag = 'e';
    }
    
    public AnnotationValue(Utf8Info classInfo) {
    	this.classInfo = classInfo;
    	this.tag = 'c';
    }
    
    public AnnotationValue(AnnotationValue nestedValue) {
    	this.nestedValue = nestedValue;
    	this.tag = '@';
    }

	@Override
	public void read(IByteBuffer source, long offset) {
		tag = (char)source.readUnsignedShort(offset);
		
		
		if (isPrimitiveValue()) {
			primitiveValueIndex = source.readUnsignedShort(offset+1);
		} else if (isEnumValue()) {
			enumTypeNameIndex = source.readUnsignedShort(offset+1);
			enumConstNameIndex = source.readUnsignedShort(offset+3);
		} else if (isClassValue()) {
			classInfoIndex = source.readUnsignedShort(offset+1);
		} else if (isNested()) {
			nestedValue = new AnnotationValue();
			nestedValue.read(source, offset+1);
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
		
	}
	

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, tag);
		if (isPrimitiveValue()) {
			target.writeUnsignedShort(offset+1, primitiveValueEntry.getIndexInPool());
		} else if (isEnumValue()) {
			target.writeUnsignedShort(offset+1, enumTypeName.getIndexInPool());
			target.writeUnsignedShort(offset+3, enumConstName.getIndexInPool());
		} else if (isClassValue()) {
			target.writeUnsignedShort(offset+1, classInfo.getIndexInPool());
		} else if (isNested()) {
			nestedValue.write(target, offset+1);
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
			return 1+nestedValue.getLength();
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
	}

	@Override
	public boolean isStructure() {
		return isNested();
	}

	@Override
	public List<IPrintable> getStructureParts() {
		if (isNested()) {
			List<IPrintable> result = new ArrayList<IPrintable>();
			result.add(nestedValue);
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
		return "element value";
	}

	@Override
	public String getPrintArgs() {
		if (isPrimitiveValue()) {
			return primitiveValueEntry.getPrintLabel();
		} else if (isEnumValue()) {
			return enumConstName.getPrintLabel()+", "+enumTypeName.getPrintLabel();
		} else if (isClassValue()) {
			return classInfo.getPrintLabel();
		} else if (isNested()) {
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
			} else {
				throw new IllegalStateException("Wrong entry: "+primitiveValueEntry);
			}
		} else if (isEnumValue()) {
			return enumConstName.getValue()+", "+enumTypeName.getValue();
		} else if (isClassValue()) {
			return classInfo.getValue();
		} else if (isNested()) {
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
			nestedValue.resolve();
		} else {
			throw new IllegalStateException("illegal tag : "+tag);
		}
		
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
		return (tag == 'e');
	}
	
	public boolean isNested() {
		return (tag == '@');
	}

}
