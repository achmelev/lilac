package org.jasm.item;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;

public interface IBytecodeItem {
	
	public void read(IByteBuffer source, long offset);
	public void write(IByteBuffer target, long offset);
	public void resolve();
	public int getLength();
	
	
	//PrintMethods
	public boolean isStructure();
	public List<IBytecodeItem> getStructureParts();
	public String  getPrintLabel();
	public String  getPrintName();
	public String  getPrintArgs();
	public String  getPrintComment();
	

}
