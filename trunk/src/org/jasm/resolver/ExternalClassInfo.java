package org.jasm.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.Method;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class ExternalClassInfo extends AbstractInfo {
	
	String name;
	String superName;
	ExternalClassInfo superClass;
	List<String> interfacesNames = new ArrayList<String>();
	List<ExternalClassInfo> interfaces = new ArrayList<ExternalClassInfo>();
	ClassModifier modifier;
	List<MethodInfo> methods = new ArrayList<MethodInfo>();
	List<FieldInfo> fields = new ArrayList<FieldInfo>();
	
	boolean isArray = false;
	TypeDescriptor descriptor;
	ExternalClassInfo componentClass;
	
	private Map<String, MethodInfo> methodRegistry = new HashMap<String, MethodInfo>();
	private Map<String, FieldInfo> fieldRegistry = new HashMap<String, FieldInfo>();
	private Map<String, List<MethodInfo>> methodsByName = new HashMap<String, List<MethodInfo>>();
	
	
	
	public String getName() {
		return name;
	}
	
	public String getPackage() {
		if (isArray) {
			return "java/lang";
		}
		if (name.indexOf('/')>=0) {
			return name.substring(0, name.lastIndexOf('/'));
		} else {
			return "";
		}
	}
	
	public boolean isAssignableTo(ExternalClassInfo info) {
		if (info.name.equals(name)) {
			return true;
		}
		if (isArray) {
			if (info.isArray) {
				if (getDescriptor().isPrimitive()) {
					return info.getDescriptor().isPrimitive() && getDescriptor().equals(info.getDescriptor());
				} else {
					return getComponentClass().isAssignableTo(info.getComponentClass());
				}
			} else {
				return false;
			}
		} else {
			if (info.isArray) {
				return false;
			} else {
				boolean result = false;
				if (superClass != null) {
					result = result || superClass.isAssignableTo(info);
				}
				for (ExternalClassInfo intf: interfaces) {
					result = result || intf.isAssignableTo(info);
				}
				return result;
			}
		}
		
	}
	
	public boolean isDerivedFrom(ExternalClassInfo info) {
		if (info.isArray) {
			return true;
		} else if (this.isArray) {
			return info.getName().equals("java/lang/Object");
		} else {
			if (name.equals(info.getName())) {
				return true;
			} else if (superClass != null) {
				return superClass.isDerivedFrom(info);
			} else {
				return false;
			}
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
	

	public ExternalClassInfo getComponentClass() {
		return componentClass;
	}

	

	public void updateMetaData() {
		methodRegistry.clear();
		for (MethodInfo mi: methods) {
			methodRegistry.put(mi.getName()+"@"+mi.getDescriptor().getValue(),mi);
			if (methodsByName.containsKey(mi.getName())) {
				List<MethodInfo> infos = methodsByName.get(mi.getName());
				infos.add(mi);
			} else {
				List<MethodInfo> infos = new ArrayList<MethodInfo>();
				infos.add(mi);
				methodsByName.put(mi.getName(), infos);
			}
		}
		fieldRegistry.clear();
		for (FieldInfo fi: fields) {
			fieldRegistry.put(fi.getName()+"@"+fi.getDescriptor().getValue(),fi);
		}
	}
	
	public void registerVirtualField(FieldInfo fi) {
		fieldRegistry.put(fi.getName()+"@"+fi.getDescriptor().getValue(),fi);
	}
	
	public void registerVirtualMethod(MethodInfo fi) {
		methodRegistry.put(fi.getName()+"@"+fi.getDescriptor().getValue(),fi);
	}
	
	public List<MethodInfo> getMethodByName(String name) {
		return methodsByName.get(name);
	}
	
	public MethodInfo getMethod(String name, String descriptor) {
		return methodRegistry.get(name+"@"+descriptor);
	}
	
	public FieldInfo getField(String name, String descriptor) {
		return fieldRegistry.get(name+"@"+descriptor);
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
