package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IConstantPoolReference;

public class ExceptionHandler extends AbstractByteCodeItem implements IConstantPoolReference {
	
	private int startPC = -1;
	private int endPC = -1;
	private int handlerPC = -1;
	private int catchTypeIndex = -1;
	private ClassInfo catchType = null;
	
	public ExceptionHandler() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		this.startPC = source.readUnsignedShort(offset);
		this.endPC = source.readUnsignedShort(offset+2);
		this.handlerPC = source.readUnsignedShort(offset+4);
		this.catchTypeIndex = source.readUnsignedShort(offset+6);

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, startPC);
		target.writeUnsignedShort(offset+2, endPC);
		target.writeUnsignedShort(offset+4, handlerPC);
		if (catchType != null) {
			target.writeUnsignedShort(offset+6, catchType.getIndexInPool());
		} else {
			target.writeUnsignedShort(offset+6, 0);
		}

	}

	@Override
	public int getLength() {
		return 8;
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
		return "exception handler";
	}

	@Override
	public String getPrintArgs() {
		return startPC+", "+endPC+", "+handlerPC+", "+(catchType==null?JasmConsts.NIL:catchType.getPrintLabel());
	}

	@Override
	public String getPrintComment() {
		if (catchType != null) {
			return catchType.getClassName();
		}
		return null;
	}

	@Override
	protected void doResolve() {
		if (catchTypeIndex>0) {
			catchType = (ClassInfo)getConstantPool().get(catchTypeIndex-1);
		}

	}

	@Override
	public AbstractConstantPoolEntry[] getReference() {
		return new AbstractConstantPoolEntry[]{catchType};
	}

}
