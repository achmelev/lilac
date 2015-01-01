package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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




public class Verifier {
	
	private Instructions parent;
	private Map<Integer, Subroutine> subroutines = new HashMap<Integer, Subroutine>();
	private Map<Integer, Subroutine> subroutineRets = new HashMap<Integer, Subroutine>();
	
	List<Set<Integer>> followers = new ArrayList<Set<Integer>>();
	Set<Integer> deadInstructions = new HashSet();
	Set<Integer> fallOffs = new HashSet<Integer>();
	List<Set<ExceptionHandler>> exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
	
	public void setParent(Instructions parent) {
		this.parent = parent;
	}
	
	AbstractInstruction getInstructionAt(int index) {
		return parent.get(index);
	}
	
	public Set<Integer> getAllFollowersFor(int index) {
		Set<Integer> result = new HashSet<Integer>();
		result.addAll(followers.get(index));
		for (ExceptionHandler handler: exceptionHandlers.get(index)) {
			result.add(handler.getIndex());
		}
		
		return result;
	}
	
	public Set<Integer> getAllReachable(int index) {
		Set<Integer> result = new HashSet<Integer>();
		getAllReachable(index, result);
		return result;
	}
	
	public Set<Integer> getAllReachable() {
		Set<Integer> result = new HashSet<Integer>();
		for (Integer i: subroutines.keySet()) {
			result.addAll(subroutines.get(i).getInstructions());
		}
		return result;
	}
	
	private void getAllReachable(int index, Set<Integer> result) {
		List<Integer> unvisited = new ArrayList<Integer>();
		unvisited.add(index);
		while (unvisited.size() > 0) {
			Integer i = unvisited.remove((int)0);
			result.add(i);
			for (Integer f: followers.get(i)) {
				if (!unvisited.contains(f) && !result.contains(f)) {
					unvisited.add(f);
				}
			}
		}
		
	}
	
	public void verify() {
		calculateFollowers();
		calculateSubroutines();
		checkFallOffs();
		
	}
	
	private void calculateSubroutines() {
		calculateSubroutine(0, true);
	}
	
	private void calculateSubroutine(int start, boolean main) {
		Subroutine r = new  Subroutine(start, main);
		r.setParent(this);
		if (!subroutines.containsKey(start)) {
			r.calculate();
			subroutines.put(start, r);
			for (Integer ret: r.getRets()) {
				if (!subroutineRets.containsKey(ret)) {
					subroutineRets.put(ret, r);
				} else {
					throw new VerifyException(ret, "multiple subroutines must not share the same ret");
				}
			}
			for (Integer call: r.getSubroutineCalls()) {
				calculateSubroutine(call, false);
			}
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
				if (ai.isReturn() || ai.getOpCode() == OpCodes.athrow) {
					//Returns and throws don't have followers
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						fallOffs.add(instr.getIndex());
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
						fallOffs.add(instr.getIndex());
					} else {
						instrFollowers.add(nextIndex);
					}
				}
			} else {
				int nextIndex = instr.getIndex()+1;
				if (nextIndex>=parent.getSize()) {
					fallOffs.add(instr.getIndex());
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
	
	private void checkFallOffs() {
		Set<Integer> reachables = getAllReachable();
		for (Integer i: fallOffs) {
			if (reachables.contains(i)) {
				throw new VerifyException(i, "execution falls here off the code end");
			}
		}
	}
	

}
