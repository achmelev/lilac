package org.jasm.item;

import org.jasm.bytebuffer.IByteBuffer;

public interface IBytecodeItem {
	
	public void read(IByteBuffer source, long offset);
	public void write(IByteBuffer target, long offset);
	public void resolve();
	public int getLength();

}
