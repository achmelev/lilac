package org.jasm.item.instructions.verify;

import java.util.List;

import org.jasm.item.instructions.verify.types.VerificationType;


public class FullFrame extends AbstractFrameDifference {
	
	private List<VerificationType> stack;
	private List<VerificationType> locals;
	
	public FullFrame(List<VerificationType> locals, List<VerificationType> stack) {
		this.locals = locals;
		this.stack = stack;
	}

	public List<VerificationType> getLocals() {
		return locals;
	}

	public List<VerificationType> getStack() {
		return stack;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FullFrame other = (FullFrame) obj;
		if (other.locals.size() !=this.locals.size()) {
			return false;
		}
		if (other.stack.size() != this.stack.size()) {
			return false;
		}
		
		for (int i=0;i<this.stack.size(); i++) {
			if (!this.stack.get(i).equals(other.stack.get(i))) {
				return false;
			}
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			if (!this.locals.get(i).equals(other.locals.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
