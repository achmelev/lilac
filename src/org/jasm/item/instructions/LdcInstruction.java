package org.jasm.item.instructions;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.parser.literals.SymbolReference;

public class LdcInstruction extends AbstractInstruction implements IConstantPoolReference {
	
	private int cpEntryIndex = -1;
	private SymbolReference cpEntryReference = null;
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
		return cpEntry.getSymbolName();
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
	protected void doResolveAfterParse() {
		cpEntry = getConstantPool().checkAndLoadFromSymbolTable(new Class[]{StringInfo.class,IntegerInfo.class,FloatInfo.class,ClassInfo.class,MethodHandleInfo.class,MethodTypeInfo.class}, cpEntryReference);
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{cpEntry};
	}

	public void setCpEntryReference(SymbolReference cpEntryReference) {
		this.cpEntryReference = cpEntryReference;
	}
	
	

}
