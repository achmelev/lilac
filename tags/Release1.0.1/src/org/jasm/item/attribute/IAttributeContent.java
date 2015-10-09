package org.jasm.item.attribute;

import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;

public interface IAttributeContent extends IBytecodeItem, IPrintable {
	
	public void prepareRead(int length);

}