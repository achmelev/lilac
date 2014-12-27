package org.jasm.item.classpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.Method;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;
import org.junit.runner.Result;

public class ExternalClassInfo {
	
	private String name;
	private String superName;
	private ExternalClassInfo superClass;
	private List<String> interfacesNames = new ArrayList<String>();
	private List<ExternalClassInfo> interfaces = new ArrayList<ExternalClassInfo>();
	private ClassModifier modifier;
	private List<MethodInfo> methods = new ArrayList<MethodInfo>();
	private List<FieldInfo> fields = new ArrayList<FieldInfo>();
	
	private boolean isArray = false;
	private TypeDescriptor descriptor;
	private ExternalClassInfo componentClass;
	
	private Map<String, MethodInfo> methodRegistry = new HashMap<String, MethodInfo>();
	private Map<String, FieldInfo> fieldRegistry = new HashMap<String, FieldInfo>();
	
	private boolean invalid = false;
	private boolean resolved = false;
	
	public String getName() {
		return name;
	}
	
	public String getPackage() {
		if (isArray) {
			throw new IllegalStateException("Arrays have no packages");
		}
		if (name.indexOf('/')>=0) {
			return name.substring(0, name.lastIndexOf('/'));
		} else {
			return "";
		}
	}
	

	public String getSuperName() {
		return superName;
	}

	public List<String> getInterfacesNames() {
		return interfacesNames;
	}



	public List<ExternalClassInfo> getInterfaces() {
		return interfaces;
	}
	
	public ExternalClassInfo getSuperClass() {
		return superClass;
	}

	public ClassModifier getModifier() {
		return modifier;
	}
	
	public List<MethodInfo> getMethods() {
		return methods;
	}
	
	public List<FieldInfo> getFields() {
		return fields;
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	
	public TypeDescriptor getDescriptor() {
		return descriptor;
	}
	
	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}
	
	

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public void updateMetaData() {
		methodRegistry.clear();
		for (MethodInfo mi: methods) {
			methodRegistry.put(mi.getName()+"@"+mi.getDescriptor().getValue(),mi);
		}
		fieldRegistry.clear();
		for (FieldInfo fi: fields) {
			fieldRegistry.put(fi.getName()+"@"+fi.getDescriptor().getValue(),fi);
		}
	}
	
	public MethodInfo getMethod(String name, String descriptor) {
		return methodRegistry.get(name+"@"+descriptor);
	}
	
	public FieldInfo getField(String name, String descriptor) {
		return fieldRegistry.get(name+"@"+descriptor);
	}
	
	
	
	
	
	
	public static ExternalClassInfo resolve(Clazz clazz, AbstractByteCodeItem caller, SymbolReference symbol, String className, boolean checkAccess)  {
		if (className.startsWith("[")) {
			return resolveArray(clazz, caller, symbol, className);
		} else {
			ExternalClassInfo result =  resolveClass(clazz, caller, symbol, className);
			if (checkAccess) {
				if (result.getModifier().isPublic()) {
					//OK
				} else {
					if (clazz.getPackage().equals(result.getPackage())) {
						//OK
					} else {
						caller.emitError(symbol, "tried illegal access for "+result.name);
						return null;
					}
				}
			}
			return result;
		}
		
	}
	
	private static ExternalClassInfo resolveArray(Clazz clazz, AbstractByteCodeItem caller, SymbolReference symbol, String className) {
		ExternalClassInfo result = new ExternalClassInfo();
		TypeDescriptor desc = new TypeDescriptor(className);
		result.descriptor = desc;
		result.superName = "java/lang/Object";
		ExternalClassInfo superInfo = resolve(clazz, caller, symbol, result.superName, false);
		if (superInfo != null && !superInfo.isInvalid()) {
			result.superClass = superInfo;
			if (desc.getComponentType().isArray() || desc.getComponentType().isObject()) {
				result.componentClass = resolve(clazz, caller, symbol, desc.getComponentType().getValue(), true);
				if (result.componentClass != null) {
					return result;
				} else {
					return null;
				}
			} else {
				return result;
			}
		} else {
			return null;
		}
		
	}
	
	private static ExternalClassInfo resolveClass(Clazz clazz, AbstractByteCodeItem caller, SymbolReference symbol, String className) {
		ExternalClassInfo result = clazz.findClass(className);
		if (result == null || result.isInvalid()) {
			caller.emitError(symbol, "unknown class "+className);
			return null;
		} else if (result.isResolved()) {
			return result;
		} else {
			if (result.superName != null) {
				ExternalClassInfo superInfo = resolve(clazz, caller, symbol, result.superName, false);
				if (superInfo != null) {
					result.superClass = superInfo;
				} else {
					result.setInvalid(true);
					return null;
				}
			}
			for (String name: result.interfacesNames) {
				ExternalClassInfo intfInfo = resolve(clazz, caller, symbol, name, false);
				if (intfInfo != null) {
					result.interfaces.add(intfInfo);
				} else {
					result.setInvalid(true);
					return null;
				}
			}
			return result;
		}
	}
	
	
	public static ExternalClassInfo createFromClass(Clazz clazz) {
		ExternalClassInfo result = new ExternalClassInfo();
		
		result.name = clazz.getThisClass().getClassName();
		if (clazz.getSuperClass() != null) {
			result.superName = clazz.getSuperClass().getClassName();
		}
		result.modifier = clazz.getModifier();
		
		for (org.jasm.item.constantpool.ClassInfo clinfo: clazz.getInterfaces()) {
			result.interfacesNames.add(clinfo.getClassName());
		}
		
		for (int i=0;i<clazz.getMethods().getSize(); i++) {
			Method meth = clazz.getMethods().get(i);
			MethodInfo info = new MethodInfo();
			info.setName(meth.getName().getValue());
			info.setDescriptor(new MethodDescriptor(meth.getDescriptor().getValue()));
			info.setParent(result);
			info.setModifier(meth.getModifier());
			result.methods.add(info);
		}
		
		for (int i=0;i<clazz.getFields().getSize(); i++) {
			Field field = clazz.getFields().get(i);
			FieldInfo info = new FieldInfo();
			info.setName(field.getName().getValue());
			info.setDescriptor(new TypeDescriptor(field.getDescriptor().getValue()));
			info.setParent(result);
			info.setModifier(field.getModifier());
			result.fields.add(info);
		}
		
		result.updateMetaData();
		
		return result;
	}
	
	

}
