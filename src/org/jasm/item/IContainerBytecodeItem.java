package org.jasm.item;

import org.jasm.item.constantpool.ConstantPool;


public interface IContainerBytecodeItem<T extends IBytecodeItem> extends IBytecodeItem {

	
	public int getSize();
	public T get(int index);
	public int indexOf(T item);
	public int getItemSizeInList(IBytecodeItem item);
	
	public ConstantPool getConstantPool();
	

}
