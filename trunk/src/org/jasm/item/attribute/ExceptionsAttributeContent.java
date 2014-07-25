package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IPrimitiveValueReferencingEntry;

public class ExceptionsAttributeContent extends AbstractAttributeContent {
	
	private int [] indexes = null; 
	private ClassInfo[] classInfos = null;
	
	public ExceptionsAttributeContent(ClassInfo[] classInfos) {
		this.classInfos = classInfos;
	}
	
	public ExceptionsAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		int numberOfInfos = source.readUnsignedShort(offset);
		this.indexes = new int[numberOfInfos];
		for (int i=0;i<numberOfInfos; i++) {
			indexes[i] = source.readUnsignedShort(offset+2*(i+1));
		}
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, classInfos.length);
		for (int i=0;i<classInfos.length; i++) {
			target.writeUnsignedShort(offset+2*(i+1), classInfos[i].getIndexInPool());
		}
	}

	@Override
	public int getLength() {
		return (classInfos.length+1)*2;
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
		return "exceptions";
	}

	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		buf.append(((Attribute)getParent()).getName().getPrintLabel());
		for (ClassInfo cli: classInfos) {
			buf.append(", ");
			buf.append(cli.getPrintLabel());
		}
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		buf.append(((Attribute)getParent()).getName().getValue());
		for (ClassInfo cli: classInfos) {
			buf.append(", ");
			buf.append(cli.getClassName());
		}
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		classInfos = new ClassInfo[indexes.length];
		for (int i=0;i<indexes.length; i++) {
			classInfos[i] = (ClassInfo)getConstantPool().get(indexes[i]-1);
		}
	}

	
	
	public ClassInfo[] getClassInfos() {
		return classInfos;
	}

	public List<String> getExceptionClassNames() {
		List<String> result = new ArrayList<>();
		for (ClassInfo cli: classInfos) {
			result.add(cli.getClassName());
		}
		return result;
	}

}
