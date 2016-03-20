package org.jasm.item.instructions.verify;

import org.jasm.item.instructions.verify.types.VerificationType;

public class SameLocalsOneStackItemFrame extends AbstractFrameDifference {
	
	private VerificationType stackItem;
	
	public SameLocalsOneStackItemFrame(VerificationType t) {
		this.stackItem = t;
	}

	public VerificationType getStackItem() {
		return stackItem;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SameLocalsOneStackItemFrame other = (SameLocalsOneStackItemFrame) obj;
		if (stackItem == null) {
			if (other.stackItem != null)
				return false;
		} else if (!stackItem.equals(other.stackItem))
			return false;
		return true;
	}
	
	

}
