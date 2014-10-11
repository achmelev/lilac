package org.jasm.item.instructions;

import org.jasm.JasmConsts;
import org.jasm.parser.literals.SymbolReference;

public class LocalVariable implements Comparable<LocalVariable> {
	
	private SymbolReference name;
	private SymbolReference parentName;
	private LocalVariable parent;
	private int offset;
	private LocalVariablesPool pool;
	private int index;
	private char type;
	
	private boolean resolved = false;
	private boolean hasResolveErrors = false;
	private boolean offsetSet = false;
	
	private boolean resolving = false;
	
	public LocalVariable(int index, char type) {
		this.index = index;
		this.type = type;
	}
	
	public LocalVariable(char type) {
		this.index = -1;
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
		return type+"_"+index;
	}

	@Override
	public int compareTo(LocalVariable o) {
		int result = new Integer(index+getLength()).compareTo(o.getIndex()+o.getLength()); 
		if (result == 0) {
			result = new Integer(index).compareTo(new Integer(o.getIndex()));
		}
		return result;
		
	}
	
	public int getLength() {
		if (type == JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE || type == JasmConsts.LOCAL_VARIABLE_TYPE_LONG) {
			return 2;
		} else {
			return 1;
		}
	}
	
	public void resolveParent() {
		if (resolving) {
			hasResolveErrors = true;
			pool.emitError(parentName, "found cyclical references in the variables list");
		} else {
			resolving = true;
			if (parentName != null) {
				LocalVariable parent = pool.getByName(parentName.getSymbolName());
				if (parent == null) {
					hasResolveErrors = true;
					pool.emitError(parentName, "unknown variable name: "+parentName.getSymbolName());
				} else {
					if (!parent.isResolved()) {
						parent.resolveParent();
					}
					if (!parent.hasResolveErrors) {
						this.parent = parent;
					} else {
						hasResolveErrors = true;
						pool.emitError(parentName, "unknown variable name: "+parentName.getSymbolName());
					}
				}
			}
			resolving = false;
		}
		resolved = true;
	}
	
	public boolean isAncestor(LocalVariable var) {
		if (parent == null) {
			return false;
		} else if (parent == var) {
			return true;
		} else {
			return parent.isAncestor(var); 
		}
	}

	public int calculateIndex(int lastOffset) {
		if (parent == null && !isOffsetSet()) {
			index = lastOffset;
		} else if (parent == null && isOffsetSet()) {
			if (offset<0) {
				pool.emitError(name, "illegal offset");
				index = offset;
			} else {
				index = offset;
			}
		} else if (parent != null ) {
			index = parent.getIndex();
			if (index < 0) {
				
			} else {
				if (isOffsetSet()) {
					index = index+offset;
					if (index <0) {
						pool.emitError(name, "illegal offset");
					} 
				}
			}
		}
		return index;
	}

	public SymbolReference getName() {
		return name;
	}

	public void setName(SymbolReference name) {
		this.name = name;
	}

	public void setParentName(SymbolReference parentName) {
		this.parentName = parentName;
	}
	
	

	public SymbolReference getParentName() {
		return parentName;
	}

	public void setParent(LocalVariable parent) {
		this.parent = parent;
	}

	

	public void setOffset(int offset) {
		this.offset = offset;
		offsetSet = true;
	}
	
	

	public int getOffset() {
		return offset;
	}

	public boolean isOffsetSet() {
		return offsetSet;
	}

	public void setPool(LocalVariablesPool pool) {
		this.pool = pool;
	}

	public boolean isResolved() {
		return resolved;
	}

	public boolean isHasResolveErrors() {
		return hasResolveErrors;
	}
	
	public static String getTypeName(char typeCode) {
		String type;
		if (typeCode == JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE) {
			type = JasmConsts.TYPENAME_OBJECT;
		} else if (typeCode  == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			type = JasmConsts.TYPENAME_RETURNADRESS;
		} else if (typeCode  == JasmConsts.LOCAL_VARIABLE_TYPE_INT) {
			type = JasmConsts.TYPENAME_INT;
		} else if (typeCode  == JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT) {
			type = JasmConsts.TYPENAME_FLOAT;
		} else if (typeCode  == JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE) {
			type = JasmConsts.TYPENAME_DOUBLE;
		} else if (typeCode  == JasmConsts.LOCAL_VARIABLE_TYPE_LONG) {
			type = JasmConsts.TYPENAME_LONG;
		} else {
			throw new IllegalStateException("Unknown type: "+typeCode);
		}
		
		return type;
	}
	
	

}
