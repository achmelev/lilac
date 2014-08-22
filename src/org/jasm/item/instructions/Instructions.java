package org.jasm.item.instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.map.KeyToListMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Instructions extends AbstractByteCodeItem implements IContainerBytecodeItem<AbstractInstruction>, IPrintable {
	
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Map<Integer, AbstractInstruction> offsets = new HashMap<>();
	
	private List<AbstractInstruction> items = new ArrayList<>();
	
	private KeyToListMap<AbstractInstruction, IBytecodeItem> instructionReferences = new KeyToListMap<>();
	
	private Set<LocalVariable> localVariableReferences = new HashSet<>();
	
	
	@Override
	public String getPrintName() {
		return "instructions";
	}
	
	

	private AbstractInstruction createEmptyItem(IByteBuffer source,
			long offset) {
		short opCode = source.readUnsignedByte(offset);
		
		if (OpCodes.isArgumentLessInstruction(opCode)) {
			return new ArgumentLessInstruction(opCode);
		} else if (OpCodes.isLocalVariableInstruction(opCode)) {
			return new LocalVariableInstruction(opCode, (short)-1);
		} else if (OpCodes.isShortLocalVariableInstruction(opCode)) {
			return new ShortLocalVariableInstruction(opCode);
		} else if (OpCodes.isConstantPoolInstruction(opCode)) {
			return new ConstantPoolInstruction(opCode, null);
		} else if (OpCodes.isBranchInstruction(opCode)) {
			return new BranchInstruction(opCode, null);
		} else if (OpCodes.ldc == opCode) {
			return new LdcInstruction(null);
		} else if (OpCodes.bipush == opCode) {
			return new BipushInstruction((byte)-1);
		} else if (OpCodes.iinc == opCode) {
			return new IincInstruction((short)-1,(byte)-1);
		} else if (OpCodes.invokeinterface == opCode) {
			return new InvokeInterfaceInstruction(opCode,null);
		} else if (OpCodes.lookupswitch == opCode) {
			return new LookupSwitchInstruction();
		} else if (OpCodes.tableswitch == opCode) {
			return new TableSwitchInstruction();
		} else if (OpCodes.multianewarray == opCode) {
			return new MultianewarrayInstruction();
		} else if (OpCodes.newarray == opCode) {
			return new NewarrayInstruction();
		} else if (OpCodes.sipush == opCode) {
			return new SipushInstruction((short)-1);
		} else {
			throw new RuntimeException("Unknown op code: "+Integer.toHexString(opCode)+" at offset "+offset);
		}
		
		
	}

	
	private void setOffsets() {
		offsets.clear();
		for (int i=0;i<items.size(); i++) {
			AbstractInstruction instruction = items.get(i);
			offsets.put(instruction.getOffsetInCode(), instruction);
		}
	}



	public void add(AbstractInstruction item) {
		items.add(item);
		setOffsets();
	}



	public void add(int index, AbstractInstruction item) {
		items.add(index, item);
		setOffsets();
	}


	public void remove(AbstractInstruction item) {
		items.remove(item);
		setOffsets();
	}
	
	public AbstractInstruction getInstructionAtOffset(int offset) {
		if (!offsets.containsKey(offset)) {
			throw new IllegalArgumentException("Illegal Offset: "+offset);
		}
		return offsets.get(offset);
	}



	@Override
	public void read(IByteBuffer source, long offset) {
		long currentOffset = offset;
		long codeLength = source.readUnsignedInt(offset);
		if (codeLength > Integer.MAX_VALUE) {
			throw new RuntimeException("Super long methods not supported!");
		}
		if (log.isDebugEnabled()) {
			log.debug("Reading instructions,offset="+currentOffset+" codeLength = "+codeLength);
		}
		currentOffset+=4;
		while (currentOffset<(offset+codeLength+4)) {
			AbstractInstruction instr = createEmptyItem(source, currentOffset);
			instr.setParent(this);
			items.add(instr);
			offsets.put(instr.getOffsetInCode(), instr);
			instr.read(source, currentOffset+1);
			
			if (log.isDebugEnabled()) {
				log.debug("Read instruction "+instr.getPrintName()+" at offset = "+currentOffset+", length="+instr.getLength()+", offsetInCode="+instr.getOffsetInCode());
			}
			
			
			currentOffset+=instr.getLength();
		}
		if (currentOffset!=(offset+codeLength+4)) {
			throw new IllegalStateException("last instruction out of code bound: "+currentOffset+"!="+(offset+codeLength+4));
		}
		
	}



	@Override
	public void write(IByteBuffer target, long offset) {
		long currentOffset = offset;
		long codeLength = getLength()-4;
		if (codeLength > Integer.MAX_VALUE) {
			throw new RuntimeException("Super long methods not supported!");
		}
		if (log.isDebugEnabled()) {
			log.debug("Writing instructions,offset="+currentOffset+", codeLength = "+codeLength);
		}
		target.writeUnsignedInt(currentOffset, codeLength);
		currentOffset+=4;
		for (AbstractInstruction instr: items) {
			target.writeUnsignedByte(currentOffset, instr.getOpCode());
			instr.write(target, currentOffset+1);
			if (log.isDebugEnabled()) {
				log.debug("Written instruction "+instr.getPrintName()+" at offset = "+currentOffset+", length="+instr.getLength());
			}
			currentOffset+=instr.getLength();
		}
		if (currentOffset!=(offset+codeLength+4)) {
			throw new IllegalStateException("last instruction out of code bound: "+currentOffset+"!="+(offset+codeLength+4));
		}
		
	}



	@Override
	public int getLength() {
		int result = 4;
		for (IBytecodeItem item: items) {
			if (item != null) {
				result+=item.getLength();
			}
		}
		return result;
	}



	@Override
	public boolean isStructure() {
		return true;
	}



	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		
		for (LocalVariable loc: localVariableReferences) {
			String type = null;
			if (loc.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE) {
				type = JasmConsts.TYPENAME_OBJECT;
			} else if (loc.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_INT) {
				type = JasmConsts.TYPENAME_INT;
			} else if (loc.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT) {
				type = JasmConsts.TYPENAME_FLOAT;
			} else if (loc.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE) {
				type = JasmConsts.TYPENAME_DOUBLE;
			} else if (loc.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_LONG) {
				type = JasmConsts.TYPENAME_LONG;
			} else {
				throw new IllegalStateException("Unknown type: "+loc.getType());
			}
			result.add(new SimplePrintable(null, "declare "+type, new String[]{loc.toString(),loc.getIndex()+""}, (String[])null));
			
		}
		
		result.addAll(items);
		return result;
	}



	@Override
	public String getPrintLabel() {
		return null;
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
	public int getSize() {
		return items.size();
	}



	@Override
	public AbstractInstruction get(int index) {
		return items.get(index);
	}



	@Override
	public int indexOf(AbstractInstruction item) {
		return items.indexOf(item);
	}



	@Override
	protected void doResolve() {
		for (AbstractInstruction instr: items) {
			instr.resolve();
		}
		
	}



	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}



	@Override
	protected void doUpdateMetadata() {
		instructionReferences.clear();
		localVariableReferences.clear();

		List<IBytecodeItem> allItems = ((AbstractByteCodeItem)getParent()).getAllItemsFromHere();
		for (IBytecodeItem item: allItems) {
			if (item instanceof IInstructionReference) {
				IInstructionReference ir = (IInstructionReference)item;
				AbstractInstruction[] refs = ir.getInstructionReferences();
				for (AbstractInstruction r:refs) {
					if (!items.contains(r)) {
						throw new IllegalStateException(r+" isn't in list!");
					}
					instructionReferences.addToList(r, item);
				}
			}
			if (item instanceof ILocalVariableReference) {
				ILocalVariableReference lref = (ILocalVariableReference)item;
				for (LocalVariable l: lref.getLocalVariableReferences()) {
					localVariableReferences.add(l);
				}
			}
		}
		
				
	}
	
	public List<IBytecodeItem> getReferencingItems(AbstractInstruction ir) {
		if (!items.contains(ir)) {
			throw new IllegalArgumentException("Entry insn't in pool: "+ir);
		}
		return instructionReferences.get(ir);
	}

	public int getCodeLength() {
		return getLength()-4;
	}
	
	
	

}
