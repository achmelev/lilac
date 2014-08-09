package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;

public class LdcInstruction extends AbstractInstruction implements IConstantPoolReference {
	
	private int cpEntryIndex = -1;
	private AbstractConstantPoolEntry cpEntry = null; 
	
	
	
	public LdcInstruction(AbstractConstantPoolEntry entry) {
		super(OpCodes.ldc);
		this.cpEntry = entry;
	}

	@Override
	public int getLength() {
		return 2;
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
	public String getPrintArgs() {
		return cpEntry.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return cpEntry.getPrintComment();
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		cpEntryIndex = source.readUnsignedByte(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, (short)cpEntry.getIndexInPool());
	}

	@Override
	protected void doResolve() {
		cpEntry = getConstantPool().get(cpEntryIndex-1);
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{cpEntry};
	}

}
