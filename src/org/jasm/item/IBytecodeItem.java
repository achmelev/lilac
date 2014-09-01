package org.jasm.item;


import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.SourceLocation;

public interface IBytecodeItem {
	
	public void read(IByteBuffer source, long offset);
	public void write(IByteBuffer target, long offset);
	public void resolve();
	public boolean hasResolveErrors();
	public void updateMetadata();
	public int getLength();
	public String getTypeLabel();
	
	
	//Tree
	public boolean isRoot();
	public IContainerBytecodeItem  getParent();
	public <T> T getAncestor(Class<T> type);
	public void setParent(IContainerBytecodeItem  parent);
	
	//Source
	public SourceLocation getNextSourceLocation();
	

}
