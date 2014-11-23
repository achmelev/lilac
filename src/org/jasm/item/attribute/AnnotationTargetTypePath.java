package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;

public class AnnotationTargetTypePath extends AbstractByteCodeItem {
	
	private short [] pathKinds = null;
	private short [] argumentIndexes = null;
	
	public AnnotationTargetTypePath() {
		
	}
	
	public AnnotationTargetTypePath (short [] pathKinds, short [] argumentIndexes) {
		this.pathKinds = pathKinds;
		this.argumentIndexes = argumentIndexes;
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
	public String getTypeLabel() {
		return null;
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
					buf.append("type argument ["+argumentIndexes[i]+"]");
				} else if (pathKinds[i] == JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_TYPE_BOUND) {
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
	protected void doResolveAfterParse() {
		
	}

}
