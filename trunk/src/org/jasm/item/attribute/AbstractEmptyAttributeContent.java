package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;


public abstract class AbstractEmptyAttributeContent extends AbstractSimpleAttributeContent {
	
	
	public AbstractEmptyAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		
	}

	@Override
	public int getLength() {
		return 0;
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
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		
	}

	

}
