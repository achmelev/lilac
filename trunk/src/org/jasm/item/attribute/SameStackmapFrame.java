package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;

public class SameStackmapFrame extends AbstractStackmapFrame {
	
	public SameStackmapFrame() {
		super((short)0);
	}

	@Override
	public int getLength() {
		return 1;
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
		return "same";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		
	}

	@Override
	protected void doWriteBody(IByteBuffer source, long offset) {
		
	}

	@Override
	protected void doResolveBody() {
		
	}

	@Override
	protected void doResolveBodyAfterParse() {
		
	}

	@Override
	protected boolean offsetCodedInTag() {
		return true;
	}

	@Override
	protected short calculateTag(short tagRangeBegin) {
		return (short)(tagRangeBegin+calculateDeltaOffset());
	}

}
