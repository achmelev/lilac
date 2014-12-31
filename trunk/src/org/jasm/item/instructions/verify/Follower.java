package org.jasm.item.instructions.verify;

public class Follower {
	
	private int index;
	private boolean exception;
	
	Follower(int index, boolean exception) {
		this.index = index;
		this.exception = exception;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isException() {
		return exception;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (exception ? 1231 : 1237);
		result = prime * result + index;
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
		Follower other = (Follower) obj;
		if (exception != other.exception)
			return false;
		if (index != other.index)
			return false;
		return true;
	}
	
	
	

}
