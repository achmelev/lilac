package org.jasm.item;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;

public abstract class AbstractBytecodeItemList<T extends IBytecodeItem> implements IBytecodeItem {
	
	private List<T> items = new ArrayList<>();
	private int size = 0;
	
	@Override
	public void read(IByteBuffer source, long offset) {
		items.clear();
		long currentOffset = offset;
		size = source.readUnsignedShort(currentOffset)-getSizeDiff();
		currentOffset+=2;
		for (int i=0;i<size; i++) {
			T item = createEmptyItem(source, currentOffset);
			item.read(source, currentOffset);
			items.add(item);
			currentOffset+=item.getLength();
		}
		
	}
	
	@Override
	public void write(IByteBuffer target, long offset) {
		int currentOffset = 0;
		target.writeUnsignedShort(offset, size+getSizeDiff());
		currentOffset+=2;
		for (IBytecodeItem item: items) {
			item.write(target, currentOffset);
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
