package org.jasm.item.instructions.verify;


public class ChopFrame extends AbstractFrameDifference {
	
	private int count;
	
	public ChopFrame(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChopFrame other = (ChopFrame) obj;
		if (count != other.count)
			return false;
		return true;
	}
	
	
}
