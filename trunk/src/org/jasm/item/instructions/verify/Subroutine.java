package org.jasm.item.instructions.verify;

import java.util.HashSet;
import java.util.Set;

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
	
	
	
	

}
