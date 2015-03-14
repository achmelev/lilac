package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.parser.literals.SymbolReference;

public class ObjectStackmapVariableinfo extends AbstractStackmapVariableinfo implements IConstantPoolReference {
	
	private int classInfoindex;
	private ClassInfo classInfo;
	private SymbolReference classInfoReference;

	public ObjectStackmapVariableinfo() {
		super((short)7);
	}

	@Override
	public int getLength() {
		return 3;
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
		return "object "+classInfo.getSymbolName();
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
		classInfo = (ClassInfo)getConstantPool().get(classInfoindex-1);
	}
	
	

	@Override
	protected void doVerify() {
		
	}

	@Override
	protected void doResolveAfterParse() {
		classInfo = getConstantPool().checkAndLoadFromSymbolTable(this, ClassInfo.class, classInfoReference);
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		classInfoindex = source.readUnsignedShort(offset+1);
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, classInfo.getIndexInPool());
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{classInfo};
	}

	public void setClassInfoReference(SymbolReference classInfoReference) {
		this.classInfoReference = classInfoReference;
	}

	public ClassInfo getClassInfo() {
		return classInfo;
	}
	
	
	

}
