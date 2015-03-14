package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.parser.literals.SymbolReference;

public class CatchAnnotationTargetType extends AbstractAnnotationTargetType implements IExceptionHandlerReference {
	
	private SymbolReference handlerReference;
	private int handlerIndex = -1;
	private ExceptionHandler handler;


	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		handlerIndex = source.readUnsignedShort(offset+1);
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, handler.getIndex());
		
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
		return "targets catch type";
	}

	@Override
	public String getPrintArgs() {
		return handler.getPrintLabel();
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		CodeAttributeContent code = (CodeAttributeContent)getAncestor(CodeAttributeContent.class);
		handler = code.getExceptionTable().get(handlerIndex);
	}
	
	

	@Override
	protected void doVerify() {
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (isInCode()) {
			CodeAttributeContent code = (CodeAttributeContent)getAncestor(CodeAttributeContent.class);
			handler = code.getExceptionTable().checkAndLoadFromSymbolTable(this, handlerReference);
		} else {
			emitIllegalInContextError();
		}
	}

	@Override
	public ExceptionHandler[] getExceptionHandlerReferences() {
		return new ExceptionHandler[]{handler};
	}

	public void setHandlerReference(SymbolReference handlerReference) {
		this.handlerReference = handlerReference;
	}

	

}
