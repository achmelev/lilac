package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;

public class ChopStackmapFrame extends AbstractStackmapFrame {
	
	private short k;
	private IntegerLiteral kLiteral;
	
	public ChopStackmapFrame() {
		super((short)248);
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
		return "chop";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel()+", "+k;
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
		k = (short)(251-tagValue);
	}

	@Override
	protected void doResolveBodyAfterParse() {
		if (kLiteral.isValid()) {
			int iValue = kLiteral.getValue();
			if (iValue<1 || iValue>3) {
				emitError(kLiteral, "locals number out of bounds!");
			} else {
				k = (short)iValue;
			}
		} else {
			emitError(kLiteral, "malformed integer or integer out of bounds");
		}
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag(short tagRangeBegin) {
		return (short)(251-k);
	}

	public void setkLiteral(IntegerLiteral kLiteral) {
		this.kLiteral = kLiteral;
	}
	
	
	
	

}
