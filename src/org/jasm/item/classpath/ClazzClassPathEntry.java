package org.jasm.item.classpath;

import java.util.HashMap;
import java.util.Map;

import org.jasm.item.clazz.Clazz;

public class ClazzClassPathEntry implements IClassPathEntry {
	
	private Map<String, ClassInfo> classes = new HashMap<String, ClassInfo>();

	@Override
	public ClassInfo findClass(String className) {
		return classes.get(className);
	}
	
	public void add(Clazz cl) {
		ClassInfo cli = ClassInfo.createFromClass(cl);
		if (classes.containsKey(cli.getName())) {
			throw new IllegalArgumentException("Already registered!");
		}
		classes.put(cli.getName(), cli);
		
	}

}
