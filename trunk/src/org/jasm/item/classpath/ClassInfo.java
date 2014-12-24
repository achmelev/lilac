package org.jasm.item.classpath;

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
import org.junit.runner.Result;

public class ClassInfo {
	
	private String name;
	private String superName;
	private List<String> interfaces = new ArrayList<String>();
	private ClassModifier modifier;
	private List<MethodInfo> methods = new ArrayList<MethodInfo>();
	private List<FieldInfo> fields = new ArrayList<FieldInfo>();
	
	private Map<String, MethodInfo> methodRegistry = new HashMap<String, MethodInfo>();
	private Map<String, FieldInfo> fieldRegistry = new HashMap<String, FieldInfo>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuperName() {
		return superName;
	}
	public void setSuperName(String superName) {
		this.superName = superName;
	}
	public List<String> getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}
	public ClassModifier getModifier() {
		return modifier;
	}
	public void setModifier(ClassModifier modifier) {
		this.modifier = modifier;
	}
	public List<MethodInfo> getMethods() {
		return methods;
	}
	public void setMethods(List<MethodInfo> methods) {
		this.methods = methods;
	}
	public List<FieldInfo> getFields() {
		return fields;
	}
	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
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
	
	public static ClassInfo createFromClass(Clazz clazz) {
		ClassInfo result = new ClassInfo();
		
		result.setName(clazz.getThisClass().getClassName());
		result.setSuperName(clazz.getSuperClass().getClassName());
		result.setModifier(clazz.getModifier());
		
		for (org.jasm.item.constantpool.ClassInfo clinfo: clazz.getInterfaces()) {
			result.getInterfaces().add(clinfo.getClassName());
		}
		
		for (int i=0;i<clazz.getMethods().getSize(); i++) {
			Method meth = clazz.getMethods().get(i);
			MethodInfo info = new MethodInfo();
			info.setName(meth.getName().getValue());
			info.setDescriptor(new MethodDescriptor(meth.getDescriptor().getValue()));
			info.setParent(result);
			info.setModifier(meth.getModifier());
			result.getMethods().add(info);
		}
		
		for (int i=0;i<clazz.getFields().getSize(); i++) {
			Field field = clazz.getFields().get(i);
			FieldInfo info = new FieldInfo();
			info.setName(field.getName().getValue());
			info.setDescriptor(new TypeDescriptor(field.getDescriptor().getValue()));
			info.setParent(result);
			info.setModifier(field.getModifier());
			result.getFields().add(info);
		}
		
		result.updateMetaData();
		
		return result;
	}
	
	

}
