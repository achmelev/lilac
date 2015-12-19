package org.jasm.item;


import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.SourceLocation;
import org.jasm.type.verifier.VerifierParams;

public interface IBytecodeItem {
	
	public void read(IByteBuffer source, long offset);
	public void write(IByteBuffer target, long offset);
	public void resolve();
	public void verify();
	public boolean hasErrors();
	public void updateMetadata();
	public int getLength();
	public boolean isGenerated();;
	
	
	//Tree
	public boolean isRoot();
	public IContainerBytecodeItem  getParent();
	public <T> T getAncestor(Class<T> type);
	public void setParent(IContainerBytecodeItem  parent);
	
	//Source
	public SourceLocation getNextSourceLocation();
	public SourceLocation getSourceLocation();
	

}
