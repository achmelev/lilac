package org.jasm.item.instructions.verify;

import java.util.HashSet;
import java.util.Set;

import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;

class Subroutine {
	
	private Verifier parent = null;
	
	private boolean main;
	private Integer start;
	private Set<Integer> rets = new HashSet<Integer>();
	private Set<Integer> instructions = new HashSet<Integer>();
	private Set<Integer> subroutineCalls = new HashSet<Integer>();
	
	
	Subroutine(int start, boolean main) {
		this.start = start;
		this.main = main;
	}
	
	void calculate() {
		Set<Integer> allInstructions = parent.getAllReachable(start);
		for (Integer i: allInstructions) {
			AbstractInstruction instr = parent.getInstructionAt(i);
			if (instr.getOpCode() == OpCodes.ret) {
				rets.add(i);
			} else if (instr.getOpCode() == OpCodes.jsr 
					|| instr.getOpCode() == OpCodes.jsr_w) {
				subroutineCalls.add(i);
			}
			instructions.add(i);
		}
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subroutine other = (Subroutine) obj;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	public boolean isMain() {
		return main;
	}

	public Integer getStart() {
		return start;
	}

	public Set<Integer> getRets() {
		return rets;
	}

	public Set<Integer> getSubroutineCalls() {
		return subroutineCalls;
	}

	public void setParent(Verifier parent) {
		this.parent = parent;
	}

	public Set<Integer> getInstructions() {
		return instructions;
	}
	
	
	
	
	

}
