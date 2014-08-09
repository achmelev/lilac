package org.jasm.test.clazz;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Assert;



import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.attribute.ConstantValueAttributeContent;
import org.jasm.item.attribute.DeprecatedAttributeContent;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.test.item.DummyRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class Class2Test {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/Class2.class");
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		byte [] buf = new byte[1024];
		try {
			int read = stream.read(buf);
			while (read>=0) {
				if (read>0) {
					bo.write(buf, 0, read);
				}
				read = stream.read(buf);
			}
			bo.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		byte [] data = bo.toByteArray();
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		
		Clazz clazz = new Clazz();
		clazz.read(bbuf, 0L);
		clazz.resolve();
		clazz.updateMetadata();
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		PrettyPrinter printer = new PrettyPrinter(writer);
		printer.printItem(clazz);
		writer.close();
		log.debug("code: \n"+sw.toString());
		
		assertEquals(clazz.getThisClass().getClassName(),"org/jasm/test/testclass/Class2");
		assertEquals(clazz.getSuperClass().getClassName(),"java/lang/Object");
		assertEquals(clazz.getInterfaces().size(), 0);
		assertEquals(clazz.getAttributes().getSize(), 2);
		assertTrue(clazz.getModifier().isPublic());
		assertTrue(clazz.getModifier().isAbstract());
		assertFalse(clazz.getModifier().isAnnotation());
		assertFalse(clazz.getModifier().isEnum());
		assertFalse(clazz.getModifier().isFinal());
		assertFalse(clazz.getModifier().isInterface());
		assertTrue(clazz.getModifier().isSuper());
		
		ClassInfo ci = clazz.getConstantPool().getClassInfo("org/jasm/test/testclass/Class2");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(ci).contains(clazz));
		
		assertEquals("org/jasm/test/testclass/Class2$1", ((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(0).getInnerClassName());
		assertNull(((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(0).getOuterClassName());
		assertNull(((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(0).getInnerName());
		
		assertEquals("org/jasm/test/testclass/Class2$InnerClass", ((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(1).getInnerClassName());
		assertEquals("org/jasm/test/testclass/Class2", ((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(1).getOuterClassName());
		assertEquals("InnerClass", ((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(1).getInnerNameValue());
		assertTrue(((InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent()).get(1).getModifier().isPrivate());
		
		String name = "staticString"; String descriptor = "Ljava/lang/String;";
		assertNotNull(clazz.getFields().getField(name, descriptor));
		Utf8Info utf8 = clazz.getConstantPool().getUtf8Info("staticString");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(utf8).contains(clazz.getFields().getField(name, descriptor)));
		
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isEnum());
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isFinal());
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isPrivate());
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isProtected());
		assertTrue(clazz.getFields().getField(name, descriptor).getModifier().isPublic());
		assertTrue(clazz.getFields().getField(name, descriptor).getModifier().isStatic());
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isSyntetic());
		assertFalse(clazz.getFields().getField(name, descriptor).getModifier().isTransient());
		
		name = "finalIntField"; 
		descriptor = "I";
		assertEquals(((ConstantValueAttributeContent)clazz.getFields().getField(name, descriptor).getAttributes().get(0).getContent()).getValue(), new Integer(0));
		
		
		
		name = "constInt"; 
		descriptor = "I";
		assertEquals(((ConstantValueAttributeContent)clazz.getFields().getField(name, descriptor).getAttributes().get(0).getContent()).getValue(), new Integer(1));
		
		name = "methodMitException"; 
		descriptor = "()V";
		assertEquals(((ExceptionsAttributeContent)clazz.getMethods().getMethod(name, descriptor).getAttributes().get(0).getContent()).getExceptionClassNames().size(),1);
		assertEquals(((ExceptionsAttributeContent)clazz.getMethods().getMethod(name, descriptor).getAttributes().get(0).getContent()).getExceptionClassNames().get(0),"java/lang/IllegalArgumentException");
		
		utf8 = clazz.getConstantPool().getUtf8Info("methodMitException");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(utf8).contains(clazz.getMethods().getMethod(name, descriptor)));
		
		name = "privateMethod"; 
		descriptor = "(I)V";
		assertTrue(clazz.getMethods().getMethod(name, descriptor).getAttributes().get(0).getContent() instanceof DeprecatedAttributeContent);
		
		
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
		
	}

}
