package org.jasm.item.instructions.verify;

public class VerifyException extends RuntimeException {
	
	private int instructionIndex = -1;
	
	public VerifyException(int index, String message) {
		super(message);
		this.instructionIndex = index;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}
	
	
	
	

}
