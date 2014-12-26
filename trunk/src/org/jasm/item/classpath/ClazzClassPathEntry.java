package org.jasm.item.classpath;

import java.util.HashMap;
import java.util.Map;

import org.jasm.item.clazz.Clazz;

public class ClazzClassPathEntry implements IClassPathEntry {
	
	private Map<String, ExternalClassInfo> classes = new HashMap<String, ExternalClassInfo>();

	@Override
	public ExternalClassInfo findClass(String className) {
		return classes.get(className);
	}
	
	public void add(Clazz cl) {
		ExternalClassInfo cli = ExternalClassInfo.createFromClass(cl);
		if (classes.containsKey(cli.getName())) {
			throw new IllegalArgumentException("Already registered!");
		}
		classes.put(cli.getName(), cli);
		
	}

}
