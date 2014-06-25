package org.jasm.item;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import org.jasm.bytebuffer.IByteBuffer;
import org.reflections.Reflections;

public abstract class AbstractTaggedBytecodeItemList<T extends ITaggedBytecodeItem> extends AbstractBytecodeItemList<T> {
	
	private static Map<Short, Class> registry = null;
	
	
	public AbstractTaggedBytecodeItemList(Class<T> clazz, String packageName) {
		if (registry == null) {
			registry = new HashMap<>();
			Reflections rf = new Reflections(packageName);
			Set<Class<? extends T>> classes = rf.getSubTypesOf(clazz);
			for (Class<? extends T> cl: classes) {
				int m = cl.getModifiers();
				if (Modifier.isPublic(m) && !Modifier.isAbstract(m)) {
					try {
						T instance = cl.newInstance();
						short tag = instance.getTag();
						if (registry.containsKey(tag)) {
							throw new RuntimeException(registry.get(tag).getName()+ " already registered for tag "+tag+", so cannot register "+cl.getName());
						} else {
							registry.put(tag, cl);
						}
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	

	@Override
	protected T createEmptyItem(IByteBuffer source, long offset) {
		short tag = source.readUnsignedByte(offset);
		if (registry.containsKey(tag)) {
			Class cl = registry.get(tag);
			T result = null;
			try {
				result = (T) cl.newInstance();
				return result;
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("No class registered for tag "+tag);
		}
	}



	

}
