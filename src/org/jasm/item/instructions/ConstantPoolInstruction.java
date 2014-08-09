package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;

public class ConstantPoolInstruction extends AbstractInstruction {
	
	private int cpEntryIndex = -1;
	private AbstractConstantPoolEntry cpEntry = null; 
	
	
	
	public ConstantPoolInstruction(short opCode, AbstractConstantPoolEntry entry) {
		super(opCode);
		this.cpEntry = entry;
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
	public String getPrintArgs() {
		return cpEntry.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return cpEntry.getPrintComment();
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		cpEntryIndex = source.readUnsignedShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, cpEntry.getIndexInPool());
	}

	@Override
	protected void doResolve() {
		cpEntry = getConstantPool().get(cpEntryIndex-1);
	}

}
