package org.jasm.item.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.map.KeyToListMap;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SymbolTable;
import org.jasm.parser.literals.SymbolReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Instructions extends AbstractByteCodeItem implements IContainerBytecodeItem<AbstractInstruction>, IPrintable {
	
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Map<Integer, AbstractInstruction> offsets ;
	
	private List<AbstractInstruction> items ;
	
	private KeyToListMap<AbstractInstruction, IBytecodeItem> instructionReferences;
	
	private Set<LocalVariable> localVariableReferences;
	
	private LocalVariablesPool variablesPool;
	
	private SymbolTable symbolTable = new SymbolTable(null);
	
	public Instructions() {
		variablesPool = new LocalVariablesPool();
		variablesPool.setParent(this);
		offsets = new HashMap<>();
		items = new ArrayList<>();
		localVariableReferences = new HashSet<>();
		instructionReferences = new KeyToListMap<>();
		
	}
	
	
	@Override
	public String getPrintName() {
		return null;
	}
	
	private AbstractInstruction createEmptyItem(IByteBuffer source,
			long offset) {
		short opCode = source.readUnsignedByte(offset);
		
		if (OpCodes.isArgumentLessInstruction(opCode)) {
			return new ArgumentLessInstruction(opCode);
		} else if (OpCodes.isLocalVariableInstruction(opCode)) {
			return new LocalVariableInstruction(opCode, false,(short)-1);
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
			return new IincInstruction(-1,(short)-1, false);
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
		} else if (OpCodes.wide == opCode) {
			short opCode2 = source.readUnsignedByte(offset+1);
			if (OpCodes.isWideFormat1Instruction(opCode2)) {
				return new LocalVariableInstruction(opCode2, true, -1);
			} else if (OpCodes.iinc == opCode2) {
				return new IincInstruction(-1,(short)-1, true);
			} else {
				throw new RuntimeException("Unknown wide op code: "+Integer.toHexString(opCode2)+" at offset "+(offset+1));
			}
		} else {
			throw new RuntimeException("Unknown op code: "+Integer.toHexString(opCode)+" at offset "+offset);
		}
		
		
	}

	
	public void setOffsets() {
		offsets.clear();
		for (int i=0;i<items.size(); i++) {
			AbstractInstruction instruction = items.get(i);
			offsets.put(instruction.getOffsetInCode(), instruction);
		}
	}
	
	
	public void addWithoutSetOffsets(AbstractInstruction item) {
		item.setParent(this);
		items.add(item);
	}


	public void add(AbstractInstruction item) {
		item.setParent(this);
		items.add(item);
		setOffsets();
	}



	public void add(int index, AbstractInstruction item) {
		item.setParent(this);
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
			if (instr.isWide()) {
				instr.read(source, currentOffset+2);
			} else {
				instr.read(source, currentOffset+1);
			}
			
			
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
			if (instr.isWide()) {
				target.writeUnsignedByte(currentOffset, OpCodes.wide);
				target.writeUnsignedByte(currentOffset+1, instr.getOpCode());
			} else {
				target.writeUnsignedByte(currentOffset, instr.getOpCode());
			}
			
			if (instr.isWide()) {
				instr.write(target, currentOffset+2);
			} else {
				instr.write(target, currentOffset+1);
			}
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
		
		List<LocalVariable> localVariableReferencesList = new ArrayList<>();
		localVariableReferencesList.addAll(localVariableReferences);
		Collections.sort(localVariableReferencesList);
		
		if (localVariableReferencesList.size() > 0) {
			result.add(new SimplePrintable(null, null, (String)null,"Variables"));
		}
		
		int lastOffset = 0;
		for (int i=0;i<localVariableReferencesList.size(); i++) {
			LocalVariable loc = localVariableReferencesList.get(i);
			String type = LocalVariable.getTypeName(loc.getType());
			
			
			String offset = getOffset(localVariableReferencesList, i, lastOffset);
			String [] args = (offset == null)?new String[]{loc.toString()}:new String[]{loc.toString()+" at "+offset};
			
			result.add(new SimplePrintable(null, "var "+type, args, (String[])null));
			lastOffset = Math.max(lastOffset, loc.getIndex()+loc.getLength());
			
		}
		if (items.size() > 0) {
			result.add(new SimplePrintable(null, null, (String)null,"Instructions"));
		}
		result.addAll(items);
		return result;
	}
	
	private String getOffset(List<LocalVariable> variables, int index, int lastOffset) {
		LocalVariable loc = variables.get(index);
		if (index == 0 && loc.getIndex()==0) {
			return null;
		} else if (index == 0 && loc.getIndex() > 0) {
			return loc.getIndex()+"";
		} else if (index>0 && loc.getIndex()==lastOffset) {
			return null;
		} else if (index>0 && loc.getIndex()!=lastOffset) {
			int gap = loc.getIndex()-variables.get(index-1).getIndex();
			if (gap == 0) {
				return variables.get(index-1).toString();
			} else {
				return variables.get(index-1).toString()+"+"+gap;
			}
		} else {
			throw new IllegalStateException("");
		}
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
	protected void doResolveAfterParse() {
		variablesPool.resolveAfterParse();
		for (AbstractInstruction instr: items) {
			instr.resolve();
		}
		
		//Replacing local var instructions with short versions
		for (int i=0;i<items.size(); i++) {
			AbstractInstruction instr = items.get(i);
			if (instr instanceof LocalVariableInstruction) {
				ShortLocalVariableInstruction newInstr = ((LocalVariableInstruction)instr).createShortReplacement();
				if (newInstr != null) {
					newInstr.setParent(this);
					items.set(i, newInstr);
					symbolTable.replace(instr, newInstr);
					newInstr.resolve();
				}
			} else if (instr instanceof BranchInstruction) {
				((BranchInstruction)instr).replaceLocalVarInstructonsWithShortVersions();
			}
		}
		
		setOffsets();
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




	@Override
	public String getTypeLabel() {
		return "instructions";
	}

	public LocalVariablesPool getVariablesPool() {
		return variablesPool;
	}


	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
	public AbstractInstruction checkAndLoadFromSymbolTable(AbstractByteCodeItem caller, SymbolReference ref) {
		AbstractInstruction result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			result = (AbstractInstruction)entry;
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown instruction label "+ref.getSymbolName());
			}
		}
		return result;
	}
	

}
