package org.jasm.item.attribute;

import java.util.List;



import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.NameAndTypeInfo;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.MethodDescriptor;

public class EnclosingMethodAttributeContent extends AbstractSimpleAttributeContent implements IConstantPoolReference {
	
	private int clazzIndex = -1;
	private SymbolReference clazzReference;
	private ClassInfo clazz = null;
	private int methodIndex = -1;
	private SymbolReference methodReference;
	private NameAndTypeInfo method = null;
	
	
	public EnclosingMethodAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.clazzIndex = source.readUnsignedShort(offset);
		this.methodIndex = source.readUnsignedShort(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, clazz.getIndexInPool());
		if (method != null) {
			target.writeUnsignedShort(offset+2, method.getIndexInPool());
		} else {
			target.writeUnsignedShort(offset+2, 0);
		}
		
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}
	
	

	
	@Override
	public String getPrintName() {
		return "enclosing method";
	}
	
	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(clazz.getSymbolName());
		if (method != null) {
			buf.append(", ");
			buf.append(method.getSymbolName());
		}
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		buf.append(clazz.getClassName());
		if (method != null) {
			buf.append(", ");
			buf.append(method.getName()+" "+method.getDescriptor());
		}
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		this.clazz = (ClassInfo)getConstantPool().get(this.clazzIndex-1);
		if (this.methodIndex == 0) {

		} else {
			this.method = (NameAndTypeInfo)getConstantPool().get(this.methodIndex-1);
		}
		
	}
	
	@Override
	protected void doResolveAfterParse() {
		this.clazz = getConstantPool().checkAndLoadFromSymbolTable(this,ClassInfo.class, clazzReference);
		if (this.clazz == null) {
			emitError(clazzReference, "unknown class info");
		}
		if (this.methodReference != null) {
			this.method = getConstantPool().checkAndLoadFromSymbolTable(this,NameAndTypeInfo.class, methodReference);
			if (this.method == null) {
				emitError(methodReference, "unknown name_and_type info");
			} else {
				try {
					MethodDescriptor desc = new MethodDescriptor(method.getDescriptor());
				} catch (IllegalDescriptorException e) {
					emitError(methodReference, "expected method but got type descriptor");
				}
			}
		}
	}

	public ClassInfo getClazz() {
		return clazz;
	}

	public NameAndTypeInfo getMethod() {
		return method;
	}

	public String getClassName() {
		return clazz.getClassName();
	}
	
	public String getMethodName() {
		return method.getName();
	}
	
	public String getMethodDescriptor() {
		return method.getDescriptor();
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		if (method == null) {
			return new AbstractConstantPoolEntry[]{clazz};
		} else {
			return new AbstractConstantPoolEntry[]{clazz,method};
		}
		
	}

	public void setClazzReference(SymbolReference clazzReference) {
		this.clazzReference = clazzReference;
	}

	public void setMethodReference(SymbolReference methodReference) {
		this.methodReference = methodReference;
	}
	
	

}
