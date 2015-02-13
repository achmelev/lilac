package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.type.verifier.VerifierParams;

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
	protected void doVerify(VerifierParams params) {
		
		
	}

	@Override
	protected void doResolveBodyAfterParse() {
		int tag = calculateTag();
		if (tag<0 || tag>63) {
			emitError(instructionReference, "instruction out of allowed interval");
		}
	}

	@Override
	protected boolean offsetCodedInTag() {
		return true;
	}

	@Override
	protected short calculateTag() {
		return (short)(tagRangeBegin+calculateDeltaOffset());
	}

}
