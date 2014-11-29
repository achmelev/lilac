package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.IImplementsDeclarationsReference;
import org.jasm.parser.literals.SymbolReference;

public class SupertypeAnnotationTargetType extends AbstractAnnotationTargetType implements IImplementsDeclarationsReference {
	
	private SymbolReference indexSymbolReference;
	private int index = -1;

	public SupertypeAnnotationTargetType() {
		super();
	}

	public SupertypeAnnotationTargetType(short targetType, short index) {
		super(targetType);
		this.index = index;
		if (index>=0 && index<=65535) {
			//OK
		} else {
			throw new IllegalArgumentException("index out of bounds: "+index);
		}
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		index = source.readUnsignedShort(offset+1);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, index);
		
	}

	@Override
	public int getLength() {
		return 3;
	}

	@Override
	public String getTypeLabel() {
		StringBuffer buf = new StringBuffer();
		buf.append("targets supertype");
		return buf.toString();
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
		return getTypeLabel();
	}

	@Override
	public String getPrintArgs() {
		if (index != JasmConsts.ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX) {
			return "implref_"+index;
		} else {
			return null;
		}
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {

		
	}

	@Override
	protected void doResolveAfterParse() {
		
		if (!isInClass()) {
			emitIllegalInContextError();
		} else {
		
			if (indexSymbolReference == null) {
				index = JasmConsts.ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX;
			} else {
				Clazz clazz = getAncestor(Clazz.class);
				Integer i = clazz.checkAndLoadInterfaceIndex(this, indexSymbolReference);
				if (i != null) {
					index = i;
				}
			}
		}
		
	}

	@Override
	public int[] getIndexes() {
		if (index != JasmConsts.ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX) {
			return new int[]{index};
		} else {
			return new int[]{};
		}
	}

	public void setIndexSymbolReference(SymbolReference indexSymbolReference) {
		this.indexSymbolReference = indexSymbolReference;
	}
	
	

}
