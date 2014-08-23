package org.jasm.item.instructions;

public class LocalVariable implements Comparable<LocalVariable> {
	
	private int index;;
	private char type;
	
	public LocalVariable(int index, char type) {
		this.index = index;
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + type;
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
		LocalVariable other = (LocalVariable) obj;
		if (index != other.index)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
	
	public int getIndex() {
		return index;
	}

	public char getType() {
		return type;
	}

	public String toString() {
		return type+"loc"+index;
	}

	@Override
	public int compareTo(LocalVariable o) {
		return new Integer(index).compareTo(new Integer(o.getIndex()));
	}
	

}
