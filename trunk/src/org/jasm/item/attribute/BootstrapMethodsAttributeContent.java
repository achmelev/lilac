package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.disassembler.NameGenerator;
import org.jasm.item.AbstractBytecodeItemList;

public class BootstrapMethodsAttributeContent extends
	AbstractBytecodeItemList<BootstrapMethod> implements IAttributeContent {
	
	private NameGenerator bootstrapNameGenerator = new NameGenerator();
	
	@Override
	public String getPrintName() {
		return null;
	}
	
	
	@Override
	public void prepareRead(int length) {
	
	}
	
	@Override
	protected BootstrapMethod createEmptyItem(IByteBuffer source, long offset) {
		return new BootstrapMethod();
	}


	public NameGenerator getBootstrapNameGenerator() {
		return bootstrapNameGenerator;
	}


	
	

}
