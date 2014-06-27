package org.jasm.item;


public interface IContainerBytecodeItem<T extends IBytecodeItem> extends IBytecodeItem {

	
	public int getSize();
	public T get(int index);
	public int indexOf(T item);

}
