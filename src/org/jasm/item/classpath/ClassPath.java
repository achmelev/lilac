package org.jasm.item.classpath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class ClassPath implements IClassPathEntry {
	
	private List<IClassPathEntry> entries = new ArrayList<IClassPathEntry>();
	private Map<String, ClassInfo> cache = new WeakHashMap<String, ClassInfo>();
	
	
	@Override
	public ClassInfo findClass(String className) {
		ClassInfo info;
		if (cache.containsKey(className)) {
			info = cache.get(className);
		} else {
			info = doFindClass(className);
			cache.put(className, info);
		}
		
		if (info.getName() == null) {
			return null;
		} else {
			return info;
		}
	}
	
	private ClassInfo doFindClass(String className) {
		for (IClassPathEntry entry: entries) {
			ClassInfo info = entry.findClass(className);
			if (info != null) {
				return info;
			}
		}
		return new ClassInfo();
	}
	
	public void add(IClassPathEntry entry) {
		entries.add(entry);
	}
	
	public void addAtBegin(IClassPathEntry entry) {
		entries.add(0, entry);
	}
	
}
