package org.jasm.item;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBytecodeItemList<T extends IBytecodeItem> implements IBytecodeItem {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<T> items = new ArrayList<>();
	private int size = 0;
	
	@Override
	public void read(IByteBuffer source, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("Reading items");
		}
		items.clear();
		long currentOffset = offset;
		size = source.readUnsignedShort(currentOffset)-getSizeDiff();
		currentOffset+=2;
		for (int i=0;i<size; i++) {
			T item = createEmptyItem(source, currentOffset);
			
			item.read(source, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("read item: "+item+";currentOffset="+currentOffset+"; item.length="+item.getLength());
			}
			items.add(item);
			currentOffset+=item.getLength();
		}
		
	}
	
	@Override
	public void write(IByteBuffer target, long offset) {
		if (log.isDebugEnabled()) {
			log.debug("Writing items");
		}
		int currentOffset = 0;
		target.writeUnsignedShort(offset, size+getSizeDiff());
		currentOffset+=2;
		for (IBytecodeItem item: items) {
			item.write(target, currentOffset);
			if (log.isDebugEnabled()) {
				log.debug("wrote item: "+item+";currentOffset="+currentOffset+"; item.length="+item.getLength());
			}
			currentOffset+=item.getLength();
		}
		
	}
	
	@Override
	public int getLength() {
		int result = 2;
		for (IBytecodeItem item: items) {
			result+=item.getLength();
		}
		return result;
	}
	
	
	
	@Override
	public void resolve() {
		for (IBytecodeItem item: items) {
			item.resolve();
		}
		
	}

	public int getSize() {
		return size;
	}
	
	public T get(int index) {
		return items.get(index);
	}
	
	public void add(T item) {
		items.add(item);
		size++;
	}
	
	public void add(int index, T item) {
		items.add(index, item);
		size++;
	}
	
	public void remove( T item) {
		if (items.remove(item)) {
			size--;
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
	

}
