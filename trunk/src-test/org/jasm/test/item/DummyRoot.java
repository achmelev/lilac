package org.jasm.test.item;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class DummyRoot extends AbstractByteCodeItem implements IContainerBytecodeItem {

	@Override
	public void read(IByteBuffer source, long offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(IByteBuffer target, long offset) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isStructure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<IBytecodeItem> getStructureParts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintArgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrintComment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doResolve() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IBytecodeItem get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
