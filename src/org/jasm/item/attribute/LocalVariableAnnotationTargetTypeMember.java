package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.IInstructionReference;
import org.jasm.item.instructions.IUnknownVariableReference;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LocalVariable;

public class LocalVariableAnnotationTargetTypeMember extends AbstractByteCodeItem implements IUnknownVariableReference, IInstructionReference {
	
	private int startPC = -1;
	private AbstractInstruction startInstruction = null;
	private int length = -1;
	private AbstractInstruction endInstruction = null;
	private int index = -1;
	private LocalVariable variable;
	
	@Override
	public void read(IByteBuffer source, long offset) {
		startPC = source.readUnsignedShort(offset);
		length = source.readUnsignedShort(offset+2);
		index = source.readUnsignedShort(offset+4);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, startInstruction.getOffsetInCode());
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		Instructions instr = code.getInstructions();
		int length = 0;
		if (endInstruction == null) {
			length = instr.getCodeLength()-startInstruction.getOffsetInCode();
		} else {
			length = endInstruction.getOffsetInCode()-startInstruction.getOffsetInCode();
		}
		target.writeUnsignedShort(offset+2, length);
		target.writeUnsignedShort(offset+4, index);
		
	}

	@Override
	public int getLength() {
		return 6;
	}

	@Override
	public String getTypeLabel() {
		return null;
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
		return "targeted var";
	}

	@Override
	public String getPrintArgs() {
		String var = index+"";
		if (variable != null) {
			var = variable.toString();
		}
		return var+", "+startInstruction.getPrintLabel()+((endInstruction==null)?"":("->"+endInstruction.getPrintLabel()));
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		CodeAttributeContent code = getAncestor(CodeAttributeContent.class);
		Instructions instr = code.getInstructions();
		startInstruction = instr.getInstructionAtOffset(startPC);
		if (startPC+length == instr.getCodeLength()) {
			
		} else {
			endInstruction = instr.getInstructionAtOffset(startPC+length);
		}
	}

	@Override
	protected void doResolveAfterParse() {
		
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int[] getVariableIndexes() {
		return new int[]{index};
	}

	@Override
	public int[] getStartOffsets() {
		return new int[]{startPC};
	}

	@Override
	public int[] getEndOffsets() {
		return new int[]{startPC+length-1};
	}

	@Override
	public void setLocalVariable(LocalVariable l) {
		variable = l;
		
	}

	@Override
	public AbstractInstruction[] getInstructionReferences() {
		if (endInstruction == null) {
			return new AbstractInstruction[]{startInstruction};
		} else {
			return new AbstractInstruction[]{startInstruction, endInstruction};
		}
	}
	
	
	
	

}
