package org.jasm.item.attribute;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.environment.Environment;
import org.jasm.item.AbstractBytecodeItemList;

public class LocalVariableTableAttributeContent extends
		AbstractBytecodeItemList<DebugLocalVariable> implements IAttributeContent {

	@Override
	public String getPrintName() {
		return "debug vars";
	}
	

	@Override
	public void prepareRead(int length) {

	}

	@Override
	protected DebugLocalVariable createEmptyItem(IByteBuffer source, long offset) {
		return new DebugLocalVariable();
	}
	
	@Override
	public boolean toOmit() {
		boolean omitDebugInfos = Environment.getBooleanValue("jdasm.omitdebuginfos");
		return omitDebugInfos;
	}

}
