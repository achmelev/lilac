package org.jasm.item;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;

public interface IBytecodeItem {
	
	public void read(IByteBuffer source, long offset);
	public void write(IByteBuffer target, long offset);
	public void resolve();
	public int getLength();
	
	
	//Tree
	public boolean isRoot();
	public IContainerBytecodeItem  getParent();
	public void setParent(IContainerBytecodeItem  parent);
	

}
