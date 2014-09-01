package org.jasm.item.attribute;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.item.instructions.Instructions;

public class ExceptionHandler extends AbstractByteCodeItem implements IConstantPoolReference, IInstructionReference {
	
	private int startPC = -1;
	private int endPC = -1;
	private int handlerPC = -1;
	
	private AbstractInstruction startInstruction;
	private AbstractInstruction endInstruction;
	private AbstractInstruction handlerInstruction;
	
	
	private int catchTypeIndex = -1;
	private ClassInfo catchType = null;
	
	public ExceptionHandler() {
		
	}
	
	public ExceptionHandler(AbstractInstruction startInstruction, AbstractInstruction endInstruction, AbstractInstruction handlerInstruction) {
		this.startInstruction = startInstruction;
		this.endInstruction = endInstruction;
		this.handlerInstruction = handlerInstruction;
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
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public String getPrintArgs() {
		return startInstruction.getPrintLabel()+", "+endInstruction.getPrintLabel()+", "+handlerInstruction.getPrintLabel()+", "+(catchType==null?JasmConsts.NIL:catchType.getSymbolName());
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
		
		Instructions instr = ((CodeAttributeContent)getParent().getParent()).getInstructions();
		startInstruction = instr.getInstructionAtOffset(startPC);
		endInstruction = instr.getInstructionAtOffset(endPC);
		handlerInstruction = instr.getInstructionAtOffset(handlerPC);

	}
	
	@Override
	protected void doResolveAfterParse() {
		throw new NotImplementedException("not implemented");
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		if (catchType != null) {
			return new AbstractConstantPoolEntry[]{catchType};
		} else {
			return new AbstractConstantPoolEntry[]{};
		}
		
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		return new AbstractInstruction[]{startInstruction,endInstruction,handlerInstruction};
	}

}
