package org.jasm.item;



public abstract class AbstractByteCodeItem implements IBytecodeItem {

	private IContainerBytecodeItem parent = null;
	
	private boolean resolved = false;
	
	public IContainerBytecodeItem  getParent() {
		return parent;
	}
	
	public void setParent(IContainerBytecodeItem  parent) {
		this.parent = parent;
	}
	
	@Override
	public void resolve() {
		if (this.resolved) {
			throw new RuntimeException("Resolve can be called only once on the same instance");
		}
		if ((this.parent == null) && !isRoot()) {
			throw new RuntimeException("Cannot resolve orphan constant pool entry");
		}
		doResolve();
		this.resolved = true;
	}
	
	protected abstract void doResolve();

	@Override
	public boolean isRoot() {
		return false;
	}
	
	protected boolean isResolved() {
		return resolved;
	}
	
	
}
