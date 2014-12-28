package org.jasm.item.classpath;

import java.util.HashMap;
import java.util.Map;

import org.jasm.item.clazz.Clazz;

public class ClazzClassPathEntry implements IClassPathEntry {
	
	private Map<String, Clazz> classes = new HashMap<String, Clazz>();
	
	public ClazzClassPathEntry() {
		
	}
	
	public ClazzClassPathEntry(Clazz cl) {
		add(cl);
	}

	@Override
	public ExternalClassInfo findClass(String className) {
		Clazz cl = classes.get(className);
		if (cl == null) {
			return null;
		} else {
			if (cl.resolveMyselfAndSuperclasses()) {
				ExternalClassInfo cli = ExternalClassInfo.createFromClass(cl);
				return cli;
				
			} else {
				return null;
			}
		}
		
	}
	
	public void add(Clazz cl) {
		classes.put(cl.getThisClass().getClassName(), cl);
	}

	@Override
	public boolean isInvalid() {
		return false;
	}

}
