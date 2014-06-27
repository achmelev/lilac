package org.jasm.item;



public abstract class AbstractByteCodeItem implements IBytecodeItem {

	private IContainerBytecodeItem parent = null;
	
	public IContainerBytecodeItem  getParent() {
		return parent;
	}
	
	public void setParent(IContainerBytecodeItem  parent) {
		this.parent = parent;
	}
	
	@Override
	public void resolve() {
		if ((this.parent == null) && !isRoot()) {
			throw new RuntimeException("Cannot resolve orphan constant pool entry");
		}
		doResolve();
	}
	
	protected abstract void doResolve();

	@Override
	public boolean isRoot() {
		return false;
	}
	
	
	
	
}
