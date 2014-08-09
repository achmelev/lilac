package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;

public class LocalVariable extends AbstractByteCodeItem implements IConstantPoolReference {
	
	private int startPC = -1;
	private int length = -1;
	private int nameIndex = -1;
	private Utf8Info name = null;
	private int descriptorIndex = -1;
	private Utf8Info descriptor = null;
	private int index = -1;

	@Override
	public void read(IByteBuffer source, long offset) {
		startPC = source.readUnsignedShort(offset);
		length = source.readUnsignedShort(offset+2);
		nameIndex = source.readUnsignedShort(offset+4);
		descriptorIndex = source.readUnsignedShort(offset+6);
		index = source.readUnsignedShort(offset+8);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, startPC);
		target.writeUnsignedShort(offset+2, length);
		target.writeUnsignedShort(offset+4, name.getIndexInPool());
		target.writeUnsignedShort(offset+6, descriptor.getIndexInPool());
		target.writeUnsignedShort(offset+8, index);
	}

	@Override
	public int getLength() {
		return 10;
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
		return "local variable";
	}

	@Override
	public String getPrintArgs() {
		return startPC+", "+length+", "+name.getPrintLabel()+", "+descriptor.getPrintLabel()+", "+index;
	}

	@Override
	public String getPrintComment() {
		return name.getValue()+" "+descriptor.getValue();
	}

	@Override
	protected void doResolve() {
		name  = (Utf8Info)getConstantPool().get(nameIndex-1);
		descriptor = (Utf8Info)getConstantPool().get(descriptorIndex-1);

	}

	@Override
	public AbstractConstantPoolEntry[] getReference() {
		return new AbstractConstantPoolEntry[]{name,descriptor};
	}

}
