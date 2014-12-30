package org.jasm.item;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.type.verifier.VerifierParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBytecodeItemList<T extends IBytecodeItem> extends AbstractByteCodeItem implements IContainerBytecodeItem<T> {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<T> items = new ArrayList<>();
	private int size = 0;
	
	@Override
	public void read(IByteBuffer source, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("Reading items, offset="+offset);
		}
		items.clear();
		long currentOffset = offset;
		if (sizeFieldLength() == 2) {
			size = source.readUnsignedShort(currentOffset)-getSizeDiff();
		} else if (sizeFieldLength() == 1){
			size = source.readUnsignedByte(currentOffset)-getSizeDiff();
		} else {
			throw new IllegalArgumentException("Illegal size length: "+sizeFieldLength());
		}
		
		currentOffset+=sizeFieldLength();
		int i = 0;
		while (i<size) {
			T item = createEmptyItem(source, currentOffset);
			
			item.read(source, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("read item "+i+"/"+size+": "+item+";currentOffset="+currentOffset+"; item.length="+item.getLength());
			}
			items.add(item);
			i++;
			if (getItemSizeInList(item)>1) {
				for (int j=0;j<getItemSizeInList(item)-1; j++) {
					i++;
					items.add(null);
				}
			}
			currentOffset+=item.getLength();
		}
		
	}
	
	@Override
	public void write(IByteBuffer target, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("Writing items, offset="+offset);
		}
		long currentOffset = offset;
		if (sizeFieldLength() == 2) {
			target.writeUnsignedShort(offset, size+getSizeDiff());
		} else if (sizeFieldLength() == 1){
			target.writeUnsignedByte(offset, (short)(size+getSizeDiff()));
		} else {
			throw new IllegalArgumentException("Illegal size length: "+sizeFieldLength());
		}
		
		currentOffset+=sizeFieldLength();
		int i=0;
		for (IBytecodeItem item: items) {
			if (item != null) {
				item.write(target, currentOffset);
				if (log.isDebugEnabled()) {
					log.debug("wrote item "+i+"/"+size+":"+item+";currentOffset="+currentOffset+"; item.length="+item.getLength());
				}
				currentOffset+=item.getLength();
				i++;
			}
		}
		
	}
	
	@Override
	public int getLength() {
		int result = sizeFieldLength();
		for (IBytecodeItem item: items) {
			if (item != null) {
				result+=item.getLength();
			}
		}
		return result;
	}
	
	
	
	@Override
	protected void doResolve() {
		for (IBytecodeItem item: items) {
			if (item != null) {
				item.setParent(this);
				item.resolve();
			}
		}
		
	}
	
	
	

	@Override
	protected void doVerify(VerifierParams params) {
		for (IBytecodeItem item: items) {
			if (item != null) {
				item.verify(params);
			}
		}
		
	}

	@Override
	protected void doResolveAfterParse() {
		doResolve();
	}

	public int getSize() {
		return size;
	}
	
	public T get(int index) {
		T result = items.get(index);
		if (result == null) {
			throw new IllegalArgumentException("Illegal index: "+index);
		}
		return result;
	}
	
	public void add(T item) {
		items.add(item);
		size++;
		if (getItemSizeInList(item)>1) {
			for (int j=0;j<getItemSizeInList(item)-1; j++) {
				size++;
				items.add(null);
			}
		}
		item.setParent(this);
	}
	
	public void add(int index, T item) {
		items.add(index, item);
		size++;
		if (getItemSizeInList(item)>1) {
			for (int j=0;j<getItemSizeInList(item)-1; j++) {
				size++;
				items.add(index+1,null);
			}
		}
		item.setParent(this);
	}
	
	public void remove( T item) {
		int index = items.indexOf(item);
		if (index >=0) {
			items.remove(index);
			item.setParent(null);
			size--;
			if (getItemSizeInList(item)>1) {
				for (int j=0;j<getItemSizeInList(item)-1; j++) {
					size--;
					if (items.get(index) != null) {
						throw new IllegalStateException("NonNull found at index: "+index);
					} else {
						items.remove(index);
					}
				}
			}
		}
	}
	
	public int indexOf(T item) {
		return items.indexOf(item);
	}
	
	

	protected List<T> getItems() {
		return items;
	}

	protected abstract T createEmptyItem(IByteBuffer source, long offset);
	
	
	protected int getSizeDiff() {
		return 0;
	}
	
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	
	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		List<T> items = getItems();
		for (T item : getItems()) {
			if (item instanceof IPrintable) {
				result.add((IPrintable)item);
			}
		}
		
		
		return result;
	}
	
	@Override
	public String getPrintLabel() {
		return null;
	}
	
	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}
	
	protected int sizeFieldLength() {
		return 2;
	}

	
	
}
