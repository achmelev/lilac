package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.environment.Environment;
import org.jasm.item.AbstractBytecodeItemList;

public class LineNumberTableAttributeContent extends
		AbstractBytecodeItemList<LineNumber> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "line numbers";
	}

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected LineNumber createEmptyItem(IByteBuffer source, long offset) {
		return new LineNumber();
	}

	@Override
	public boolean toOmit() {
		boolean omitDebugInfos = Environment.getBooleanValue("jdasm.omitdebuginfos");
		return omitDebugInfos;
	}
	
	

}
