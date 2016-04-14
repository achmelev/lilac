package org.jasm.item.instructions.verify;

import java.util.List;

import org.jasm.item.instructions.verify.types.VerificationType;


public class AppendFrame extends AbstractFrameDifference {
	
	private List<VerificationType> locals;
	
	public AppendFrame(List<VerificationType> locals) {
		this.locals = locals;
	}

	public List<VerificationType> getLocals() {
		return locals;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppendFrame other = (AppendFrame) obj;
		if (other.locals.size() !=this.locals.size()) {
			return false;
		}
		
		
		for (int i=0;i<this.locals.size(); i++) {
			if (!this.locals.get(i).isAssignableFrom(other.locals.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	
}
