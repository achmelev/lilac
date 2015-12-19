package org.jasm.item.constantpool.macros;

import org.jasm.item.constantpool.ClassInfo;
import org.jasm.parser.literals.ClassReference;

public class ClassInfoConstantMacro extends AbstractConstantMacro {
	
	private ClassReference name = null;
	
	public ClassInfoConstantMacro(ClassReference name) {
		this.name = name;
	}

	@Override
	public void resolve() {
		ClassInfo constant = parent.getOrAddClassInfo(name.getClassName());
		if (label != null) {
			registerConstant(label.getLabel(), constant);
		} else {
			String className = name.getClassName();
			String shortName = (className.lastIndexOf('/')>0)?className.substring(className.lastIndexOf('/')+1, className.length()):className;
			String longName = className.replace('/', '.');
			if (!parent.getSymbolTable().contains(shortName)) {
				registerConstant(shortName, constant);
			} else  {
				registerConstant(longName, constant);
			} 
		}
	}

}
