package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;

public class LineNumber extends AbstractByteCodeItem {
	
	private int startPC = -1;
	private int lineNumber = -1;
	

	@Override
	public void read(IByteBuffer source, long offset) {
		startPC = source.readUnsignedShort(offset);
		lineNumber = source.readUnsignedShort(offset+2);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, startPC);
		target.writeUnsignedShort(offset+2, lineNumber);
	}

	@Override
	public int getLength() {
		return 4;
	}

	@Override
	public boolean isStructure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "line number";
	}

	@Override
	public String getPrintArgs() {
		return startPC+", "+lineNumber;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

	}

}
