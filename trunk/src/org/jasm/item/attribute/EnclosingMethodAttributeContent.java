package org.jasm.item.attribute;

import java.util.List;


import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.NameAndTypeInfo;

public class EnclosingMethodAttributeContent extends AbstractSimpleAttributeContent {
	
	private int clazzIndex = -1;
	private ClassInfo clazz = null;
	private int methodIndex = -1;
	private NameAndTypeInfo method = null;
	
	public EnclosingMethodAttributeContent(ClassInfo clazz, NameAndTypeInfo method) {
		this.clazz = clazz;
		this.method = method;
	}
	
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
		target.writeUnsignedShort(offset+2, method.getIndexInPool());
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
		buf.append(clazz.getPrintLabel());
		buf.append(", ");
		buf.append(method.getPrintLabel());
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		buf.append(clazz.getClassName());
		buf.append(", ");
		buf.append(method.getName()+" "+method.getDescriptor());
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		this.clazz = (ClassInfo)getConstantPool().get(this.clazzIndex-1);
		this.method = (NameAndTypeInfo)getConstantPool().get(this.methodIndex-1);
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

}
