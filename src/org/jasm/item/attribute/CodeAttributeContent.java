package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.ATR;

import org.apache.commons.codec.binary.Hex;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class CodeAttributeContent extends AbstractSimpleAttributeContent implements IContainerBytecodeItem<IBytecodeItem> {
	
	private int maxStack;
	private int maxLocals;
	private byte[] code;
	private byte[] exceptionTable;
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
	}
	
	@Override
	public void read(IByteBuffer source, long offset) {
		long currentOffset = offset;
		maxStack = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		maxLocals = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		long codeLength = source.readUnsignedInt(currentOffset);
		currentOffset+=4;
		code = source.readByteArray(currentOffset, (int)codeLength);
		currentOffset+=code.length;
		int exceptionsTableLength = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		exceptionTable = source.readByteArray(currentOffset, exceptionsTableLength);
		currentOffset+=exceptionTable.length;
		attributes.read(source, currentOffset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		long currentOffset = offset;
		target.writeUnsignedShort(currentOffset, maxStack);
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, maxLocals);
		currentOffset+=2;
		target.writeUnsignedInt(currentOffset, code.length);
		currentOffset+=4;
		target.writeByteArray(currentOffset, code);
		currentOffset+=code.length;
		target.writeUnsignedShort(currentOffset, exceptionTable.length);
		currentOffset+=2;
		target.writeByteArray(currentOffset, exceptionTable);
		currentOffset+=exceptionTable.length;
		attributes.write(target, currentOffset);
	}

	@Override
	public int getLength() {
		return 2+2+4+code.length+2+exceptionTable.length+attributes.getLength();
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
		if (exceptionTable.length > 0) {
			result.add(new SimplePrintable(null, "exception table", "0x"+new String(Hex.encodeHex(exceptionTable)), null));
		}
		if (code.length > 0) {
			result.add(new SimplePrintable(null, "code", "0x"+new String(Hex.encodeHex(code)), null));
		}
		
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "code";
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
		attributes.resolve();

	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public IBytecodeItem get(int index) {
		if (index == 0) {
			return attributes;
		} else {
			throw new IndexOutOfBoundsException(index+"");
		}
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		if (item == attributes) {
			return 0;
		} else {
			return -1;
		}
		
	}

}
