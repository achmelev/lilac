package org.jasm.item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.type.verifier.VerifierParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class AbstractByteCodeItem implements IBytecodeItem, IPrintable, IErrorEmitter {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private IContainerBytecodeItem parent = null;
	
	private boolean resolved = false;
	
	private boolean verified = false;
	
	private SourceLocation sourceLocation = null;
	
	private boolean hasResolveErrors = false;
	
	public IContainerBytecodeItem  getParent() {
		return parent;
	}
	
	public void setParent(IContainerBytecodeItem  parent) {
		this.parent = parent;
	}
	
	@Override
	public void resolve() {
		if ((this.parent == null) && !isRoot()) {
			throw new IllegalStateException("Cannot resolve orphan item!");
		}
		if (this.resolved) {
			throw new IllegalStateException("Resolve can be called only once on the same instance");
		}
		
		if (isAfterParse()) {
			doResolveAfterParse();
		} else {
			doResolve();
		}
		this.resolved = true;
	}
	
	@Override
	public void verify(VerifierParams params) {
		if ((this.parent == null) && !isRoot()) {
			throw new IllegalStateException("Cannot verify orphan item!");
		}
		if (!isAfterParse()) {
			throw new IllegalStateException("verify can be called only aufer parse and resolve");
		}
		if (!this.resolved) {
			throw new IllegalStateException("verify can be called only after parse and resolve");
		}
		if (this.verified) {
			throw new IllegalStateException("Verify can be called only once on the same instance");
		}
		
		doVerify(params);
		this.verified = true;
	}
	
	protected abstract void doResolve();
	protected abstract void doVerify(VerifierParams params);
	protected abstract void doResolveAfterParse();

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
	
	public <T> T getAncestor(Class<T> type) {
		if (this.getParent() == null) {
			return null;
		} else if (type.isAssignableFrom(this.getParent().getClass())) {
			return (T)getParent();
		} else {
			return getParent().getAncestor(type);
		}
	}
	
	public <U> List<U> getDescendants(Class<U> type) {
		List<U> result = new ArrayList<>();
		getDescendants(result, this, type);
		return result;
	}
	
	private <U> void getDescendants(List<U> result, IBytecodeItem item, Class<U> type) {
		if (type.isAssignableFrom(item.getClass())) {
			result.add((U)item);
		}
		if (item instanceof IContainerBytecodeItem) {
			List<IBytecodeItem> children = getItemsList((IContainerBytecodeItem<IBytecodeItem>)item);
			for (IBytecodeItem child: children) {
				getDescendants(result, child, type);
			}
		}
	}

	@Override
	public void updateMetadata() {
		if (!this.resolved) {
			throw new RuntimeException("updateMetadata can be called only after resolve");
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
		return getDescendants(IBytecodeItem.class);
	}
	

	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}
	
	//Methods for "after parse"-resolving and verification
	
	protected boolean isAfterParse() {
		return this.getRoot().getParser() != null;
	}
	
	protected Clazz getRoot() {
		if (this instanceof Clazz) {
			return (Clazz)this;
		} else {
			return getAncestor(Clazz.class);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.jasm.item.IErrorEmitter#emitError(org.jasm.parser.literals.AbstractLiteral, java.lang.String)
	 */
	@Override
	public void emitError(AbstractLiteral literal, String message) {
		if (literal !=  null) {
			getRoot().getParser().emitError(literal.getLine(), literal.getCharPosition(), message);
		} else {
			SourceLocation sl = getNextSourceLocation();
			getRoot().getParser().emitError(sl.getLine(), sl.getCharPosition(), message);
		}
		hasResolveErrors = true;
		
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.item.IErrorEmitter#emitErrorOnLocation(org.jasm.parser.SourceLocation, java.lang.String)
	 */
	@Override
	public void emitErrorOnLocation(SourceLocation sl, String message) {
		getRoot().getParser().emitError(sl.getLine(), sl.getCharPosition(), message);
		hasResolveErrors = true;
		
	}
	
	public SourceLocation getNextSourceLocation() {
		if (this.sourceLocation != null) {
			return sourceLocation;
		} else if (this.parent == null ) {
			throw new IllegalStateException("no source location available!");
		} else {
			return getParent().getNextSourceLocation();
		}
	}
	
	protected SourceLocation getNextSourceLocationDown() {
		if (this.sourceLocation != null) {
			return sourceLocation;
		} else  {
			List<IBytecodeItem> descendants = getAllItemsFromHere();
			for (IBytecodeItem item: descendants) {
				if (item.getSourceLocation() != null) {
					return item.getSourceLocation();
				}
			}
		}
		return null;
	}

	public boolean hasErrors() {
		return hasResolveErrors;
	}
	
	public void hasResolveErrors(boolean value) {
		this.hasResolveErrors = value;
	}
	
	protected String getContentAsBase64() {
		byte [] data = new byte[getLength()];
		ByteArrayByteBuffer buf = new ByteArrayByteBuffer(data);
		write(buf, 0);
		return "["+Base64.encodeBase64String(data)+"]";
	}
	
	protected String createItemsListArg(AbstractByteCodeItem[] args) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i=0;i<args.length; i++) {
			AbstractByteCodeItem item = args[i];
			if (i>0) {
				buf.append(",");
			}
			buf.append(item.getPrintName());
			if (item.getPrintArgs() != null) {
				buf.append(" "+item.getPrintArgs());
			}
			
		}
		buf.append("}");
		return buf.toString();
	}
	
	protected BigDecimal getClassVersion() {
		return getRoot().getDecimalVersion();
	}
	
	protected boolean classVersionAbove(String v) {
		return getRoot().getDecimalVersion().compareTo(new BigDecimal(v))>0;
	}
	
	protected boolean classVersionLess(String v) {
		return getRoot().getDecimalVersion().compareTo(new BigDecimal(v))<0;
	}
	
	protected boolean classVersionAboveOrEqual(String v) {
		return getRoot().getDecimalVersion().compareTo(new BigDecimal(v))>=0;
	}
	
	protected boolean classVersionLessOrEqual(String v) {
		return getRoot().getDecimalVersion().compareTo(new BigDecimal(v))<=0;
	}
	
}
