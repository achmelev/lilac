package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.parser.literals.IntegerLiteral;

public class AnnotationTargetTypePath extends AbstractByteCodeItem {
	
	private short [] pathKinds = null;
	private IntegerLiteral[] argumentIndexLiterals;
	private short [] argumentIndexes = null;
	
	private int currentIndex = 0;
	
	public AnnotationTargetTypePath() {
		
	}
	
	public AnnotationTargetTypePath (short [] pathKinds, short [] argumentIndexes) {
		this.pathKinds = pathKinds;
		this.argumentIndexes = argumentIndexes;
		argumentIndexLiterals = new IntegerLiteral[argumentIndexes.length];
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		short length = source.readUnsignedByte(offset);
		pathKinds = new short[length];
		argumentIndexes = new short[length];
		
		long currentOffset = offset+1;
		for (int i=0;i<length; i++) {
			pathKinds[i] = source.readUnsignedByte(currentOffset);
			argumentIndexes[i] = source.readUnsignedByte(currentOffset+1);
			currentOffset+=2;
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, (short)pathKinds.length);
		long currentOffset = offset+1;
		for (int i=0;i<pathKinds.length; i++) {
			target.writeUnsignedByte(currentOffset, pathKinds[i]);
			target.writeUnsignedByte(currentOffset+1, argumentIndexes[i]);
			currentOffset+=2;
		}
		
	}

	@Override
	public int getLength() {
		return 1+pathKinds.length*2;
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
		if (pathKinds.length == 0) {
			return null;
		} else {
			return "target path";
		}
	}

	@Override
	public String getPrintArgs() {
		if (pathKinds.length == 0) {
			return null;
		} else {
			StringBuffer buf = new StringBuffer();
			for (int i=0;i<pathKinds.length; i++) {
				if (i>0) {
					buf.append(", ");
				}
				if (pathKinds[i] == JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_ARRAY) {
					buf.append("array");
				} else if (pathKinds[i] == JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_NESTED) {
					buf.append("nested");
				} else if (pathKinds[i] == JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_TYPE_ARGUMENT) {
					buf.append("type argument("+argumentIndexes[i]+")");
				} else if (pathKinds[i] == JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_TYPE_ARGUMENT_BOUND) {
					buf.append("type argument bound");
				} else {
					throw new IllegalArgumentException("unknown bound: "+pathKinds[i]);
				}
			}
			return buf.toString();
		}
	}

	@Override
	public String getPrintComment() {
		if (pathKinds.length == 0) {
			return null;
		} else {
			return "target path";
		}
	}

	@Override
	protected void doResolve() {
		
	}
	
	

	@Override
	protected void doVerify() {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		for (int i=0;i<argumentIndexLiterals.length; i++) {
			if (argumentIndexLiterals[i] == null) {
				argumentIndexes[i] = 0;
			} else {
				if (argumentIndexLiterals[i].isValid()) {
					int iValue = argumentIndexLiterals[i].getValue();
					if (iValue<0 || iValue>255) {
						emitError(argumentIndexLiterals[i], "type argument index out of bounds!");
					} else {
						argumentIndexes[i] = (short)iValue;
					}
				} else {
					emitError(argumentIndexLiterals[i], "malformed integer or integer out of bounds");
				}
			}
		}
	}
	
	public void setArgument(short pathKind, IntegerLiteral argumentIndexLiteral ) {
		pathKinds[currentIndex] = pathKind;
		argumentIndexLiterals[currentIndex] = argumentIndexLiteral;
		currentIndex++;
	}

}
