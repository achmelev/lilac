package org.jasm.item.instructions;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.verifier.VerifierParams;

public class LdcInstruction extends AbstractInstruction implements IConstantPoolReference {
	
	private int cpEntryIndex = -1;
	private SymbolReference cpEntryReference = null;
	private AbstractConstantPoolEntry cpEntry = null; 
	
	
	
	
	public LdcInstruction(boolean wide) {
		if (wide) {
			opCode = OpCodes.ldc_w;
		} else {
			opCode = OpCodes.ldc;
		}
	}

	@Override
	public int getLength() {
		return (this.opCode==OpCodes.ldc_w)?3:2;
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
	public String getPrintName() {
		boolean wideModifier = (this.cpEntry.getIndexInPool()<=255 && opCode == OpCodes.ldc_w);
		return (wideModifier?"wide ":"")+OpCodes.getNameForOpcode(OpCodes.ldc);
	}

	@Override
	public String getPrintComment() {
		return cpEntry.getPrintComment();
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		if (opCode == OpCodes.ldc_w) {
			cpEntryIndex = source.readUnsignedShort(offset);
		} else {
			cpEntryIndex = source.readUnsignedByte(offset);
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		if (opCode == OpCodes.ldc_w) {
			target.writeUnsignedShort(offset, cpEntry.getIndexInPool());
		} else {
			target.writeUnsignedByte(offset, (short)cpEntry.getIndexInPool());
		}
		
	}

	@Override
	protected void doResolve() {
		cpEntry = getConstantPool().get(cpEntryIndex-1);
	}
	
	@Override
	protected void doResolveAfterParse() {
		cpEntry = getConstantPool().checkAndLoadFromSymbolTable(this, new Class[]{StringInfo.class,IntegerInfo.class,FloatInfo.class,ClassInfo.class,MethodHandleInfo.class,MethodTypeInfo.class}, cpEntryReference);
		if (cpEntry.getIndexInPool()>255) {
			opCode = OpCodes.ldc_w;
		}
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{cpEntry};
	}

	public void setCpEntryReference(SymbolReference cpEntryReference) {
		this.cpEntryReference = cpEntryReference;
	}

	@Override
	protected void doVerify(VerifierParams params) {
		if (cpEntry instanceof ClassInfo) {
			getRoot().checkAndLoadClassInfo(this, cpEntryReference, ((ClassInfo)cpEntry).getClassName(), true);
		}
	}

	public AbstractConstantPoolEntry getCpEntry() {
		return cpEntry;
	}
	
	
	

}
