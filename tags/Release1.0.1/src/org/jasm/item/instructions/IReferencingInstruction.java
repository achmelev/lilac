package org.jasm.item.instructions;

public interface IReferencingInstruction extends IInstructionReference {
	
	public void replaceLocalVarInstructonsWithShortVersions();

}
