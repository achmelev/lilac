package org.jasm.item;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.parser.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class AbstractByteCodeItem implements IBytecodeItem, IPrintable {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private IContainerBytecodeItem parent = null;
	
	private boolean resolved = false;
	
	private SourceLocation sourceLocation = null;
	
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
			throw new RuntimeException("Cannot resolve orphan item!");
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
	
	public ConstantPool getConstantPool() {
		return getParent().getConstantPool();
	}

	@Override
	public void updateMetadata() {
		if (!this.resolved) {
			throw new RuntimeException("updateMetadata can be called only resolve");
		}
		if (log.isDebugEnabled()) {
			log.debug("update meta data");
		}
		if (this instanceof IContainerBytecodeItem) {
			IContainerBytecodeItem<IBytecodeItem> container = (IContainerBytecodeItem<IBytecodeItem>)this;
			for (IBytecodeItem item: getItemsList(container)) {
				item.updateMetadata();
			}
		}
		doUpdateMetadata();
	}
	
	public static <U extends IBytecodeItem> List<U> getItemsList(IContainerBytecodeItem<U> container) {
		int i=0;
		List<U> result = new ArrayList<>();
		while (i<container.getSize()) {
			U item = container.get(i);
			result.add(item);
			i+=container.getItemSizeInList(item);
		}
		return result;
		
	}
	
	protected void doUpdateMetadata() {
		
	}
	
	public List<IBytecodeItem> getAllItemsFromHere() {
		List<IBytecodeItem> result = new ArrayList<>();
		getAllItemsFromHere(result, this);
		return result;
	}
	
	private void getAllItemsFromHere(List<IBytecodeItem> result, IBytecodeItem item) {
		result.add(item);
		if (item instanceof IContainerBytecodeItem) {
			List<IBytecodeItem> children = getItemsList((IContainerBytecodeItem<IBytecodeItem>)item);
			for (IBytecodeItem child: children) {
				getAllItemsFromHere(result, child);
			}
		}
	}

	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}
	
	
}
