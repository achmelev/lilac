package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.ATR;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.clazz.IAttributesContainer;
import org.jasm.item.instructions.Instructions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeAttributeContent extends AbstractSimpleAttributeContent implements IContainerBytecodeItem<IBytecodeItem>, IAttributesContainer {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private int maxStack;
	private int maxLocals;
	private Instructions instructions;
	private ExceptionHandlerTable exceptionTable;
	private Attributes attributes = null;
	
	public CodeAttributeContent(int maxStack, int maxLocals) {
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		initChildren();
	}
	
	public CodeAttributeContent() {
		initChildren();
	}
	
	private void initChildren() {
		attributes = new Attributes();
		attributes.setParent(this);
		exceptionTable = new ExceptionHandlerTable();
		exceptionTable.setParent(this);
		instructions = new Instructions();
		instructions.setParent(this);
	}
	
	@Override
	public void read(IByteBuffer source, long offset) {
		long currentOffset = offset;
		maxStack = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		maxLocals = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		instructions.read(source, currentOffset);
		currentOffset+=instructions.getLength();
		exceptionTable.read(source, currentOffset);
		currentOffset+=exceptionTable.getLength();
		attributes.read(source, currentOffset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		long currentOffset = offset;
		target.writeUnsignedShort(currentOffset, maxStack);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, maxLocals);
		currentOffset+=2;
		instructions.write(target, currentOffset);
		currentOffset+=instructions.getLength();
		exceptionTable.write(target, currentOffset);
		currentOffset+=exceptionTable.getLength();
		attributes.write(target, currentOffset);
	}

	@Override
	public int getLength() {
		return 2+2+instructions.getLength()+exceptionTable.getLength()+attributes.getLength();
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		result.add(attributes);
		result.add(new SimplePrintable(null, "maxstack", maxStack+"", null));
		result.add(new SimplePrintable(null, "maxlocals", maxLocals+"", null));
		result.add(exceptionTable);
		result.add(instructions);
		
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return null;
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
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
		instructions.resolve();
		exceptionTable.resolve();
		attributes.resolve();

	}
	
	@Override
	protected void doResolveAfterParse() {
		instructions.resolve();
		exceptionTable.resolve();
		attributes.resolve();
		
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public IBytecodeItem get(int index) {
		if (index == 0) {
			return attributes;
		} else if (index == 1) {
			return exceptionTable;
		} else if (index == 2) {
			return instructions;
		} else {
			throw new IndexOutOfBoundsException(index+"");
		}
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		if (item == attributes) {
			return 0;
		} else if (item == exceptionTable) {
			return 1;
		} else if (item == instructions) {
			return 2;
		} else {
			return -1;
		}
		
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public Instructions getInstructions() {
		return instructions;
	}

	public ExceptionHandlerTable getExceptionTable() {
		return exceptionTable;
	}

	public Attributes getAttributes() {
		return attributes;
	}
	
	

}
