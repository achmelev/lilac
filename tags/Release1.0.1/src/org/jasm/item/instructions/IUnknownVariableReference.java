package org.jasm.item.instructions;

public interface IUnknownVariableReference {
	
	public int[] getVariableIndexes();
	public int[] getStartOffsets();
	public int[] getEndOffsets();
	public void setLocalVariable(LocalVariable l);
	

}
