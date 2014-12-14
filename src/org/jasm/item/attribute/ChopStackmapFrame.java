package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.IntegerLiteral;

public class ChopStackmapFrame extends AbstractStackmapFrame {
	
	private short k;
	private IntegerLiteral kLiteral;
	
	public ChopStackmapFrame() {
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
		return "chop frame";
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
		deltaOffset = source.readUnsignedShort(offset);
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, calculateDeltaOffset());
	}

	@Override
	protected void doResolveBody() {
		k = (short)(tagValue-tagRangeBegin);
	}

	@Override
	protected void doResolveBodyAfterParse() {
		if (kLiteral.isValid()) {
			int iValue = kLiteral.getValue();
			if (iValue<0 || iValue>2) {
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
		return (short)(tagRangeBegin+k);
	}

	public void setkLiteral(IntegerLiteral kLiteral) {
		this.kLiteral = kLiteral;
	}
	
	
	
	

}
