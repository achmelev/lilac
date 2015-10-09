package org.jasm.resolver;

import junit.framework.Assert;

import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.resolver.FieldInfo;
import org.jasm.resolver.MethodInfo;
import org.junit.Test;

public class ClassPathTest {
	
	@Test
	public void testClassLoader() {
		ClassInfoResolver path = new ClassInfoResolver();
		path.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
		
		Assert.assertNull(path.findClass("java/lang/Objecgtt"));
		
		ExternalClassInfo info = path.findClass("org/jasm/test/testclass/ClassToFind");
		
		Assert.assertNotNull(info);
		testClassToFind(info);
	}
	
	private void testClassToFind(ExternalClassInfo info) {
		Assert.assertEquals("org/jasm/test/testclass/ClassToFind", info.getName());
		Assert.assertEquals("java/lang/Object", info.getSuperName());
		Assert.assertEquals(true, info.getModifier().isAbstract());
		Assert.assertEquals(true, info.getModifier().isSuper());
		Assert.assertEquals(true, info.getModifier().isPublic());
		Assert.assertEquals(false, info.getModifier().isAnnotation());
		Assert.assertEquals(false, info.getModifier().isFinal());
		Assert.assertEquals(false, info.getModifier().isInterface());
		Assert.assertEquals(false, info.getModifier().isSyntetic());
		
		Assert.assertEquals(1, info.getInterfacesNames().size());
		Assert.assertEquals("java/lang/Runnable", info.getInterfacesNames().get(0));
		
		Assert.assertEquals(2, info.getFields().size());
		FieldInfo fi = info.getField("intField", "I");
		Assert.assertNotNull(fi);
		Assert.assertEquals("intField", fi.getName());
		Assert.assertEquals("I", fi.getDescriptor().getValue());
		Assert.assertEquals(info, fi.getParent());
		Assert.assertEquals(false, fi.getModifier().isEnum());
		Assert.assertEquals(false, fi.getModifier().isFinal());
		Assert.assertEquals(true, fi.getModifier().isPrivate());
		Assert.assertEquals(false, fi.getModifier().isProtected());
		Assert.assertEquals(false, fi.getModifier().isPublic());
		Assert.assertEquals(false, fi.getModifier().isStatic());
		Assert.assertEquals(false, fi.getModifier().isSyntetic());
		Assert.assertEquals(false, fi.getModifier().isTransient());
		Assert.assertEquals(false, fi.getModifier().isVolatile());
		fi = info.getField("doubleField", "Ljava/lang/Double;");
		Assert.assertNotNull(fi);
		Assert.assertEquals("doubleField", fi.getName());
		Assert.assertEquals("Ljava/lang/Double;", fi.getDescriptor().getValue());
		Assert.assertEquals(info, fi.getParent());
		Assert.assertEquals(false, fi.getModifier().isEnum());
		Assert.assertEquals(true, fi.getModifier().isFinal());
		Assert.assertEquals(false, fi.getModifier().isPrivate());
		Assert.assertEquals(false, fi.getModifier().isProtected());
		Assert.assertEquals(true, fi.getModifier().isPublic());
		Assert.assertEquals(false, fi.getModifier().isStatic());
		Assert.assertEquals(false, fi.getModifier().isSyntetic());
		Assert.assertEquals(false, fi.getModifier().isTransient());
		Assert.assertEquals(false, fi.getModifier().isVolatile());
		
		Assert.assertEquals(3, info.getMethods().size());
		MethodInfo mi = info.getMethod("add", "(FF)Ljava/lang/Double;");
		Assert.assertNotNull(mi);
		Assert.assertEquals("add", mi.getName());
		Assert.assertEquals("(FF)Ljava/lang/Double;", mi.getDescriptor().getValue());
		Assert.assertEquals(info, mi.getParent());
		Assert.assertEquals(true, mi.getModifier().isAbstract());
		Assert.assertEquals(false, mi.getModifier().isBridge());
		Assert.assertEquals(false, mi.getModifier().isFinal());
		Assert.assertEquals(false, mi.getModifier().isNative());
		Assert.assertEquals(false, mi.getModifier().isPrivate());
		Assert.assertEquals(false, mi.getModifier().isProtected());
		Assert.assertEquals(true, mi.getModifier().isPublic());
		Assert.assertEquals(false, mi.getModifier().isStrict());
		Assert.assertEquals(false, mi.getModifier().isSynchronized());
		
		mi = info.getMethod("checkString", "(Ljava/lang/String;)Z");
		Assert.assertNotNull(mi);
		Assert.assertEquals("checkString", mi.getName());
		Assert.assertEquals("(Ljava/lang/String;)Z", mi.getDescriptor().getValue());
		Assert.assertEquals(info, mi.getParent());
		Assert.assertEquals(false, mi.getModifier().isAbstract());
		Assert.assertEquals(false, mi.getModifier().isBridge());
		Assert.assertEquals(true, mi.getModifier().isFinal());
		Assert.assertEquals(false, mi.getModifier().isNative());
		Assert.assertEquals(true, mi.getModifier().isPrivate());
		Assert.assertEquals(false, mi.getModifier().isProtected());
		Assert.assertEquals(false, mi.getModifier().isPublic());
		Assert.assertEquals(false, mi.getModifier().isStrict());
		Assert.assertEquals(true, mi.getModifier().isSynchronized());
		
	}

}
