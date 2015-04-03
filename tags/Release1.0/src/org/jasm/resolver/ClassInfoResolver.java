package org.jasm.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IErrorEmitter;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xml.internal.serialize.XHTMLSerializer;

public class ClassInfoResolver  {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<IClassPathEntry> entries = new ArrayList<IClassPathEntry>();
	
	private Map<String, AbstractInfo> cache = new HashMap<String, AbstractInfo>();
	private List<String> notFounds = new ArrayList<String>();
	private List<String> polymorphicNames = null;
	
	public synchronized ExternalClassInfo resolve(Clazz clazz,  String className, boolean checkAccess)  {
		
		
		if (notFounds.contains(className)) {
			return null;
		}
		
		if (cache.containsKey(className)) {
			AbstractInfo info = cache.get(className);
			return (ExternalClassInfo)info;
			
		}
		
		ExternalClassInfo result = null;
		
		if (className.startsWith("[")) {
			result =  resolveArray(clazz,  className);
			if (result == null) {
				notFounds.add(className);
			} else {
				cache.put(className, result);
			}
		} else {
			result =  resolveClass(clazz, className);
			if (result == null) {
				notFounds.add(className);
			} else {
				cache.put(className, result);
			}
			if (result !=null && checkAccess) {
				//String accessKey = clazz.getThisClass().getClassName()+"->"+className;
				
				if (result.getModifier().isPublic()) {
					
				} else {
					if (clazz.getPackage().equals(result.getPackage())) {
						
					} else {
						throw new ResolveIllegalAccessException();
					}
				}
				
			}
		}
		return result;
		
	}
	
	private ExternalClassInfo resolveArray(Clazz clazz,  String className) {
		ExternalClassInfo result = new ExternalClassInfo();
		TypeDescriptor desc = new TypeDescriptor(className);
		result.descriptor = desc;
		result.superName = "java/lang/Object";
		result.isArray = true;
		result.name = className;
		ExternalClassInfo superInfo = resolve(clazz,  result.superName, false);
		if (superInfo != null) {
			result.superClass = superInfo;
			if (desc.getComponentType().isArray() || desc.getComponentType().isObject()) {
				if (desc.getComponentType().isArray()) {
					result.componentClass = resolve(clazz,  desc.getComponentType().getValue(), true);
				} else {
					result.componentClass = resolve(clazz,  desc.getComponentType().getClassName(), true);
				}
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
	
	private ExternalClassInfo resolveClass(Clazz clazz,  String className) {
		ExternalClassInfo result = findClass(className);
		if (result == null) {
			return null;
		} else {
			result.descriptor = new TypeDescriptor("L"+className+";");
			if (result.superName != null) {
				ExternalClassInfo superInfo = resolve(clazz,  result.superName, false);
				if (superInfo != null) {
					result.superClass = superInfo;
				} else {
					return null;
				}
			}
			for (String name: result.interfacesNames) {
				ExternalClassInfo intfInfo = resolve(clazz,  name, false);
				if (intfInfo != null) {
					result.interfaces.add(intfInfo);
				} else {
					return null;
				}
			}
			return result;
		}
	}
	
	ExternalClassInfo findClass(String className) {
		for (IClassPathEntry entry: entries) {
			if (!entry.isInvalid()) {
				ExternalClassInfo info = entry.findClass(className);
				if (info != null) {
					return info;
				}
			}
		}
		return null;
	}
	
	public byte [] findBytes(String resourceName) {
		for (IClassPathEntry entry: entries) {
			if (!entry.isInvalid()) {
				byte[] data = entry.findBytes(resourceName);
				if (data != null) {
					return data;
				}
			}
		}
		return null;
	}
	
	
	
	private AbstractInfo resolveMember(Clazz clazz, String className,String name,  String descriptor,boolean checkAccess, MemberFindAndAccess mfa, String label) {
		
		String memberKey = className+"."+name+"@"+descriptor;
		
		if (notFounds.contains(memberKey)) {
			if (notFounds.contains(className)) {
				throw new ResolveClassNotFoundException();
			}
			return null;
		}
		
		ExternalClassInfo cli = resolve(clazz,  className, checkAccess);
		AbstractInfo result = null;
		
		if (cli != null) {
			result = mfa.lookupInClass(cli, name, descriptor);
			if (result == null) {
				result = mfa.findMember(cli, name, descriptor);
				if (result == null) {
					notFounds.add(memberKey);
				} else {
					mfa.registerVirtualMember(cli, result);
				}
				if (result != null) {
					if (checkAccess) {
						//String accessKey = clazz.getThisClass().getClassName()+"->"+memberKey;
						
						if (mfa.checkAccess(clazz, cli,result)) {
							
						} else {
							throw new ResolveIllegalAccessException();
						}
						
					}
				} 
			}
			
		} else {
			notFounds.add(memberKey);
			throw new ResolveClassNotFoundException();
		}
		return result;
	}
	
	public FieldInfo resolveField(Clazz clazz,String className,String name,  String descriptor,boolean checkAccess)  {
		MemberFindAndAccess mfa = new MemberFindAndAccess() {
			
			@Override
			public AbstractInfo findMember(ExternalClassInfo cli, String name,
					String descriptor) {
				return findField(cli, name, descriptor);
			}
			
			@Override
			public boolean checkAccess(Clazz clazz, ExternalClassInfo requestClass,
					AbstractInfo member) {
				return checkMemberAccess(clazz, requestClass, (FieldInfo)member);
			}

			@Override
			public AbstractInfo lookupInClass(ExternalClassInfo cli,
					String name, String desc) {
				return cli.getField(name, desc);
			}

			@Override
			public void registerVirtualMember(ExternalClassInfo cli, AbstractInfo info) {
				cli.registerVirtualField((FieldInfo)info);
			}
			
			
			
		};
		
		return (FieldInfo)resolveMember(clazz, className, name, descriptor, checkAccess, mfa, "field");
		
	}
	
	public MethodInfo resolveMethod(Clazz clazz,  String className,String name,  String descriptor,boolean checkAccess)  {
		MemberFindAndAccess mfa = new MemberFindAndAccess() {
			
			@Override
			public AbstractInfo findMember(ExternalClassInfo cli, String name,
					String descriptor) {
				return findMethod(clazz, cli, name, descriptor);
			}
			
			@Override
			public boolean checkAccess(Clazz clazz, ExternalClassInfo requestClass,
					AbstractInfo member) {
				return checkMemberAccess(clazz, requestClass, (MethodInfo)member);
			}

			@Override
			public AbstractInfo lookupInClass(ExternalClassInfo cli,
					String name, String desc) {
				return cli.getMethod(name, desc);
			}

			@Override
			public void registerVirtualMember(ExternalClassInfo cli,
					AbstractInfo info) {
				cli.registerVirtualMethod((MethodInfo)info);
				
			}
			
			
		};
		
		return (MethodInfo)resolveMember(clazz,  className, name, descriptor, checkAccess, mfa, "method");
		
	}
	
	public MethodInfo resolveInterfaceMethod(Clazz clazz,  String className,String name,  String descriptor,boolean checkAccess)  {
		MemberFindAndAccess mfa = new MemberFindAndAccess() {
			
			@Override
			public AbstractInfo findMember(ExternalClassInfo cli, String name,
					String descriptor) {
				return findInterfaceMethod(cli, name, descriptor);
			}
			
			@Override
			public boolean checkAccess( Clazz clazz, ExternalClassInfo requestClass,
					AbstractInfo member) {
				return checkMemberAccess(clazz, requestClass, (MethodInfo)member);
			}
			
			@Override
			public AbstractInfo lookupInClass(ExternalClassInfo cli,
					String name, String desc) {
				return cli.getMethod(name, desc);
			}

			@Override
			public void registerVirtualMember(ExternalClassInfo cli,
					AbstractInfo info) {
				cli.registerVirtualMethod((MethodInfo)info);
				
			}
			
			
		};
		
		return (MethodInfo)resolveMember(clazz, className, name, descriptor, checkAccess,  mfa, "interface method");
		
	}
	
	private FieldInfo findField(ExternalClassInfo cli, String name, String descriptor) {
		
		
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
	
	private boolean checkMemberAccess(Clazz clazz, ExternalClassInfo requestClass, AbstractMemberInfo fi) {
		
		ExternalClassInfo me = clazz.getMe();
			
		if (fi.getMemberModifier().isPublic()) {
			return true;
		} else if (fi.getMemberModifier().isProtected() && me.isDerivedFrom(requestClass)){
			if (fi.getMemberModifier().isStatic()) {
				return true;
			} else {
				if (me.isDerivedFrom(requestClass) || requestClass.isDerivedFrom(me)) {
					return true;
				} else {
					return fi.getParent().getPackage().equals(me.getPackage());
				}
			}
		} else if ((fi.getMemberModifier().isProtected() 
					|| (!fi.getMemberModifier().isPublic() && !fi.getMemberModifier().isProtected() && !fi.getMemberModifier().isPrivate()))
					&& fi.getParent().getPackage().equals(me.getPackage())
		          ) {
			
		  return true;
			
		} else if (fi.getMemberModifier().isPrivate() && me.equals(fi.getParent()) ) {
			return true;
		}
		
		return false;
	}
	
	private MethodInfo findMethod(Clazz clazz,  ExternalClassInfo cli, String name, String descriptor) {
		
		if (cli.getModifier() != null && cli.getModifier().isInterface()) {
			throw new ResolveIsInterfaceException();
		}
		
				
		MethodInfo info = findMethodInClassAndSuperClasses(clazz,  cli, name, descriptor);
		if (info != null) {
			return info;
		} else {
			return lookupSuperInterfaceMethod(cli, name, descriptor);
		}
	}
	
	private MethodInfo findInterfaceMethod(ExternalClassInfo cli, String name, String descriptor) {
		
		if (!(cli.getModifier() != null && cli.getModifier().isInterface())) {
			throw new ResolveIsntInterfaceException();
		}
		
		ExternalClassInfo object = cli.getSuperClass();
				
		MethodInfo info = cli.getMethod(name, descriptor);
		if (info != null) {
			return info;
		} else {
			info = cli.getSuperClass().getMethod(name, descriptor);
			if (info != null && info.getModifier().isPublic() && !info.getModifier().isStatic()) {
				return info;
			} else {
				return lookupSuperInterfaceMethod(cli, name, descriptor);
			}
			
			
		}
	}
	
	private MethodInfo lookupSuperInterfaceMethod(ExternalClassInfo cli, String name, String descriptor) {
		List<MethodInfo> all = new ArrayList<MethodInfo>();
		List<MethodInfo> maxSpecific = new ArrayList<MethodInfo>();
		collectSuperInterfaceMethods(all, maxSpecific, cli, cli, name, descriptor, true);
		if (maxSpecific.size() == 1) {
			return maxSpecific.get(0);
		} else if (all.size() > 0) {
			return all.get(0);
		} else {
			return null;
		}
	}
	
	private void collectSuperInterfaceMethods(List<MethodInfo> all, List<MethodInfo> maxSpecific, ExternalClassInfo root, ExternalClassInfo cli, String name, String descriptor, boolean doMaxSpecific) {
		MethodInfo found = null;
		if (root != cli && cli.getModifier() != null && cli.getModifier().isInterface()) {
			found = cli.getMethod(name, descriptor);
		}
		boolean maxSpecificAdded = false;
		if (doMaxSpecific && found != null && !found.getModifier().isPrivate() && !found.getModifier().isStatic() && !found.getModifier().isAbstract()) {
			maxSpecific.add(found);
			maxSpecificAdded = true;
		}
		if (found != null && !found.getModifier().isPrivate() && !found.getModifier().isStatic()) {
			all.add(found);
		}
		if (cli.getSuperClass() != null) {
			collectSuperInterfaceMethods(all, maxSpecific, root, cli.getSuperClass(), name, descriptor, doMaxSpecific && !maxSpecificAdded);
		}
		for (ExternalClassInfo intf: cli.getInterfaces()) {
			collectSuperInterfaceMethods(all, maxSpecific, root, intf, name, descriptor, doMaxSpecific && !maxSpecificAdded);
		}
		
	}
	
	private MethodInfo findMethodInClassAndSuperClasses(Clazz clazz,  ExternalClassInfo cli, String name, String descriptor) {
		
		
		if (isPolymorphic(clazz,cli, name, descriptor) && cli.getMethodByName(name) != null && cli.getMethodByName(name).size() == 1) {
			return cli.getMethodByName(name).get(0);
		}
		
		
		MethodInfo info = cli.getMethod(name, descriptor);
		if (info != null) {
			return info;
		} else {
			if (cli.getSuperClass() != null) {
			    return findMethodInClassAndSuperClasses(clazz,  cli.getSuperClass(), name, descriptor);
			} else {
				return null;
			}
			
		}
	}
	
	private synchronized void initPolymorphic(Clazz clazz, ExternalClassInfo cli, String name, String descriptor) {
		if (polymorphicNames == null) {
			polymorphicNames = new ArrayList<String>();
			ExternalClassInfo mhandle = resolve(clazz,  "java/lang/invoke/MethodHandle", false);
			if (mhandle != null) {
				for (MethodInfo info: mhandle.getMethods()) {
					MethodDescriptor desc = info.getDescriptor();
					boolean pml = desc.getReturnType() != null && desc.getReturnType().getValue().equals("Ljava/lang/Object;")
							&& desc.getParameters().size() == 1
							&& desc.getParameters().get(0).getValue().equals("[Ljava/lang/Object;");
					if (pml) {
						polymorphicNames.add(info.getName());
					}
					
				}
			}
		}	
	}
	
	private boolean isPolymorphic(Clazz clazz, ExternalClassInfo cli, String name, String descriptor) {
		if (polymorphicNames == null) {
			initPolymorphic(clazz,cli,name, descriptor);
		}	
		return polymorphicNames.contains(name);
	}
	
	
	public void add(IClassPathEntry entry) {
		entries.add(entry);
	}
	
	public void addAtBegin(IClassPathEntry entry) {
		entries.add(0, entry);
	}
	
	public void logStatus() {
		log.info("Cache size: "+cache.size());
	}
}

interface MemberFindAndAccess {
	AbstractInfo findMember(ExternalClassInfo cli, String name, String descriptor);
	AbstractInfo lookupInClass(ExternalClassInfo cli, String name, String desc);
	void registerVirtualMember(ExternalClassInfo cli, AbstractInfo info);
	boolean checkAccess(Clazz clazz, ExternalClassInfo requestClass, AbstractInfo member);
}
