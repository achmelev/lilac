package org.jasm.item.classpath;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.modifier.FieldModifier;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.TypeDescriptor;

public class FieldInfo {
	
	private String name;
	private FieldModifier modifier;
	private TypeDescriptor descriptor;
	private ExternalClassInfo parent;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldModifier getModifier() {
		return modifier;
	}
	public void setModifier(FieldModifier modifier) {
		this.modifier = modifier;
	}
	public TypeDescriptor getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(TypeDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	public ExternalClassInfo getParent() {
		return parent;
	}
	public void setParent(ExternalClassInfo parent) {
		this.parent = parent;
	}
	
	public static FieldInfo resolve(Clazz clazz, AbstractByteCodeItem caller, SymbolReference symbol, String className,String name,  String descriptor,boolean checkAccess)  {
		ExternalClassInfo cli = ExternalClassInfo.resolve(clazz, caller, symbol, className, checkAccess);
		if (cli != null) {
			FieldInfo result = findField(cli, name, descriptor);
			if (result != null) {
				if (checkAccess) {
					if (checkAccess(clazz, cli,result)) {
						return result;
					} else {
						caller.emitError(symbol, "lllegal access for "+cli.getName()+"."+name+"@"+descriptor);
						return null;
					}
				} else {
					return result;
				}
			} else {
				caller.emitError(symbol, "unknown field "+cli.getName()+"."+name+"@"+descriptor);
				return null;
			}
		} else {
			return null;
		}
	}
	
	private static boolean checkAccess(Clazz clazz, ExternalClassInfo requestClass, FieldInfo fi) {
		
		ExternalClassInfo me = clazz.getMe();
		String declarationClass = fi.getParent().getName();
		
		if (fi.getModifier().isPublic()) {
			return true;
		} else if (fi.getModifier().isProtected() && me.isDerivedFrom(declarationClass)){
			if (fi.getModifier().isStatic()) {
				return true;
			} else {
				return me.isDerivedFrom(requestClass.getName()) || requestClass.isDerivedFrom(me.getName());
			}
		} else if ((fi.getModifier().isProtected() 
					|| (!fi.getModifier().isPublic() && !fi.getModifier().isProtected() && !fi.getModifier().isPrivate()))
					&& fi.getParent().getPackage().equals(me.getPackage())
		          ) {
			
		  return true;
			
		} else if (fi.getModifier().isPrivate() && me.equals(fi.getParent()) ) {
			return true;
		}
		
		return false;
	}
	

	
	private static FieldInfo findField(ExternalClassInfo cli, String name, String descriptor) {
		
		
		FieldInfo info = cli.getField(name, descriptor);
		if (info != null) {
			return info;
		} else {
			int i=0;
			for (ExternalClassInfo intf: cli.getInterfaces()) {
				info = findField(intf, name, descriptor);
				if (info != null) {
					return info;
				}
				i++;
			}
			if (cli.getSuperClass() != null) {
				info = findField(cli.getSuperClass(), name, descriptor);
				if (info != null) {
					return info;
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
	}

}
