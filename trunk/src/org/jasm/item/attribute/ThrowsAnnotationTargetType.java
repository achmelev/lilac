package org.jasm.item.attribute;

import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.clazz.Method;
import org.jasm.parser.literals.SymbolReference;


public class ThrowsAnnotationTargetType extends AbstractAnnotationTargetType implements IThrowsDeclarationsReference {
	
	private SymbolReference indexSymbolReference;
	private int index = -1;

	public ThrowsAnnotationTargetType() {
		
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
		StringBuffer buf = new StringBuffer();
		buf.append("targets throws type");
		return buf.toString();
	}

	@Override
	public String getPrintArgs() {
		if (index != JasmConsts.ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX) {
			return "throwsref_"+index;
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
		
		if (!isInMethod()) {
			emitIllegalInContextError();
		} else {
			Method m = getAncestor(Method.class);
			List<Attribute> attrs = m.getAttributes().getAttributesByContentType(ExceptionsAttributeContent.class);
			ExceptionsAttributeContent content = null;
			if (attrs.size() > 0) {
				content = (ExceptionsAttributeContent)attrs.get(0).getContent();
			}
			if (content != null) {
				Integer i = content.checkAndLoadInterfaceIndex(this, indexSymbolReference);
				if (i!=null) {
					index = i;
				}
			} else {
				emitError(indexSymbolReference, "unknown throws label");
			}
		}
		
	}

	@Override
	public int[] getIndexes() {
		return new int[]{index};
	}

	public void setIndexSymbolReference(SymbolReference indexSymbolReference) {
		this.indexSymbolReference = indexSymbolReference;
	}
	
	
	

}
