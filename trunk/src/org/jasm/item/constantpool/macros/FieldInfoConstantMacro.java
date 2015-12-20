package org.jasm.item.constantpool.macros;

import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.parser.literals.ClassReference;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.SymbolReference;

public class FieldInfoConstantMacro extends AbstractConstantMacro {
	
	private JavaTypeLiteral type;
	private SymbolReference name;
	private AbstractLiteral clazz;

	@Override
	public void resolve() {
		ClassInfo classInfo = null;
		String classSymbolName = null;
		if (clazz instanceof SymbolReference) {
			SymbolReference ref = (SymbolReference)clazz;
			classInfo = parent.checkAndLoadFromSymbolTable(parent, ClassInfo.class, ref);
			classSymbolName = ref.getSymbolName();
		} else if (clazz instanceof ClassReference)  {
			ClassReference ref = (ClassReference)clazz;
			classInfo = parent.getOrAddClassInfo(ref.getClassName());
			classSymbolName = registerClassConstant(ref.getClassName(), null);
		} else {
			throw new IllegalStateException("");
		}
		
		if (classInfo != null) {
			ClassInfo ci = (ClassInfo)parent.getSymbolTable().get(classSymbolName);
			if (type.getDescriptor() != null) {
				FieldrefInfo result = parent.getOrAddFieldrefInfo(ci.getClassName(), name.getSymbolName(), type.getDescriptor().getValue());
				registerConstant(classSymbolName+"."+name.getSymbolName(), result, label);
			}	
		}   
		
		
	}

	public void setType(JavaTypeLiteral type) {
		this.type = type;
	}

	public void setName(SymbolReference name) {
		this.name = name;
	}

	public void setClazz(AbstractLiteral clazz) {
		this.clazz = clazz;
	}
	
	

}
