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
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.verify.error.BadCodeException;
import org.jasm.item.instructions.verify.error.FallOffException;
import org.jasm.type.verifier.VerifierParams;

public class Verifier {
	
	private Instructions parent;
	
	
	List<Set<Integer>> followers = new ArrayList<Set<Integer>>();
	List<Set<ExceptionHandler>> exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
	
	public void setParent(Instructions parent) {
		this.parent = parent;
	}
	
	private AbstractInstruction getInstructionAt(int index) {
		return parent.get(index);
	}
	
	private Set<Integer> getAllFollowersFor(int index) {
		Set<Integer> result = new HashSet<Integer>();
		result.addAll(followers.get(index));
		for (ExceptionHandler handler: exceptionHandlers.get(index)) {
			result.add(handler.getHandlerInstruction().getIndex());
		}
		
		return result;
	}
	
	private Set<Integer> getAllReachable(int index) {
		Set<Integer> result = new HashSet<Integer>();
		getAllReachable(index, result);
		return result;
	}

	
	private void getAllReachable(int index, Set<Integer> result) {
		List<Integer> unvisited = new ArrayList<Integer>();
		unvisited.add(index);
		while (unvisited.size() > 0) {
			Integer i = unvisited.remove((int)0);
			result.add(i);
			for (Integer f: getAllFollowersFor(i)) {
				if (!unvisited.contains(f) && !result.contains(f)) {
					unvisited.add(f);
				}
			}
		}
		
	}
	
	public void verify(VerifierParams params) {
		calculateFollowers();
		checkForBadCode();
		checkAllReachable();
		
		
	}
	
	private void checkForBadCode() {
		for (int i=0;i<parent.getSize(); i++) {
			AbstractInstruction instr = parent.get(i);
			checkForBadCode(instr);
		}
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
					//No Subroutines
				} else {
					instrFollowers.add(bi.getTargetInst().getIndex());
				}
				if (instr.getOpCode() == OpCodes.goto_ || instr.getOpCode() == OpCodes.goto_w) {
					
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(nextIndex-1);
					} else {
						instrFollowers.add(nextIndex);
					}
				}
			} else if (instr instanceof AbstractSwitchInstruction) {
				AbstractSwitchInstruction ai = (AbstractSwitchInstruction)instr;
				instrFollowers.add(ai.getDefaultTarget().getIndex());
				for (AbstractInstruction instr1: ai.getTargets()) {
					instrFollowers.add(instr1.getIndex());
				}
			} else if (instr instanceof ArgumentLessInstruction) {
				ArgumentLessInstruction ai = (ArgumentLessInstruction)instr;
				if (ai.isReturn() || ai.getOpCode() == OpCodes.athrow) {
					//Returns and throws don't have followers
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(nextIndex-1);
					} else {
						instrFollowers.add(nextIndex);
					}
					
				}
			} else if (instr instanceof LocalVariableInstruction ) {
				LocalVariableInstruction li = (LocalVariableInstruction)instr;
				if (li.getOpCode() == OpCodes.ret) {
					//Subroutines have a special handling
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(instr.getIndex());
					} else {
						instrFollowers.add(nextIndex);
					}
				}
			} else {
				int nextIndex = instr.getIndex()+1;
				if (nextIndex>=parent.getSize()) {
					throw new FallOffException(instr.getIndex());
				} else {
					instrFollowers.add(nextIndex);
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
	
	private void checkAllReachable() {
		Set<Integer> reachables = getAllReachable(0);
		for (int i=0;i<parent.getSize(); i++) {
			if (!reachables.contains(i)) {
				throw new VerifyException(i, "dead code");
			}
		}
	}
	
	
	
	private void checkForBadCode(AbstractInstruction instr) {
		if (instr.getOpCode() == OpCodes.jsr || 
			instr.getOpCode() == OpCodes.jsr_w||
			instr.getOpCode() == OpCodes.ret) {
			throw new BadCodeException(instr.getIndex());
		}
	}
	

}
