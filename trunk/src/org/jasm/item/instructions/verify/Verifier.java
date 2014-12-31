package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.ExceptionHandler;
import org.jasm.item.attribute.ExceptionHandlerTable;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.AbstractSwitchInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.OpCodes;




public class Verifier {
	
	private Instructions parent;
	private Set<Subroutine> subroutines = new HashSet<Subroutine>();
	private Set<Subroutine> newSubroutines = new HashSet<Subroutine>();
	
	List<Set<Integer>> followers = new ArrayList<Set<Integer>>();
	List<Set<ExceptionHandler>> exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
	
	public void setParent(Instructions parent) {
		this.parent = parent;
	}
	
	AbstractInstruction getInstructionAt(int index) {
		return parent.get(index);
	}
	
	public void verify() {
		calculateFollowers();
	}
	
	private void calculateFollowers() {
		followers = new ArrayList<Set<Integer>>();
		exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
		
		//Normal followers
		for (int i=0;i<parent.getSize(); i++) {
			AbstractInstruction instr = parent.get(i);
			Set<Integer> instrFollowers = new HashSet<Integer>();
			followers.add(instrFollowers);
			Set<ExceptionHandler> instrHandlers = new HashSet<ExceptionHandler>();
			exceptionHandlers.add(instrHandlers);
			if (instr instanceof BranchInstruction) {
				BranchInstruction bi = (BranchInstruction)instr;
				if (instr.getOpCode() == OpCodes.jsr || instr.getOpCode() == OpCodes.jsr_w) {
					//Subroutines have a special handling
				} else {
					instrFollowers.add(bi.getTargetInst().getIndex());
				}
			} else if (instr instanceof AbstractSwitchInstruction) {
				AbstractSwitchInstruction ai = (AbstractSwitchInstruction)instr;
				instrFollowers.add(ai.getDefaultTarget().getIndex());
				for (AbstractInstruction instr1: ai.getTargets()) {
					instrFollowers.add(instr1.getIndex());
				}
			} else if (instr instanceof ArgumentLessInstruction) {
				ArgumentLessInstruction ai = (ArgumentLessInstruction)instr;
				if (ai.isReturn() || ai.getOpCode() == OpCodes.ret || ai.getOpCode() == OpCodes.athrow) {
					//Returns and throws don't have followers
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new VerifyException(instr.getIndex(), "Execution falls off the code end");
					}
					instrFollowers.add(nextIndex);
				}
			} else {
				int nextIndex = instr.getIndex()+1;
				if (nextIndex>=parent.getSize()) {
					throw new VerifyException(instr.getIndex(), "Execution falls off the code end");
				}
			}
		}
		
		//Exception handlers
		ExceptionHandlerTable table = ((CodeAttributeContent)parent.getParent()).getExceptionTable();
		for (int i=0;i<table.getSize(); i++) {
			ExceptionHandler handler = table.get(i);
			for (int j=handler.getStartInstruction().getIndex(); j<=handler.getEndInstruction().getIndex();  j++) {
				exceptionHandlers.get(j).add(handler);
			}
		}
	}
	

}
