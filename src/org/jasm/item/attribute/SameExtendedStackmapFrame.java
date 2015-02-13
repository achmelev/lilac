package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.type.verifier.VerifierParams;

public class SameExtendedStackmapFrame extends AbstractStackmapFrame {
	
	public SameExtendedStackmapFrame() {
		super((short)251);
	}

	@Override
	public int getLength() {
		return 3;
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
		return "same extended";
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
		deltaOffset = source.readUnsignedShort(offset+1);
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, calculateDeltaOffset());
	}

	@Override
	protected void doResolveBody() {
		
	}
	
	

	@Override
	protected void doVerify(VerifierParams params) {
		
	}

	@Override
	protected void doResolveBodyAfterParse() {
		if (calculateDeltaOffset()<0) {
			emitError(instructionReference, "instruction out of allowed interval");
		}
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag() {
		return tagRangeBegin;
	}

}
