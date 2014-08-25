package org.jasm.test.clazz;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;



import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.attribute.ConstantValueAttributeContent;
import org.jasm.item.attribute.DeprecatedAttributeContent;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.IAttributeContent;
import org.jasm.item.attribute.InnerClass;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.test.item.DummyRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
		
		List<ClassInfo> ci = clazz.getConstantPool().getClassInfos("org/jasm/test/testclass/Class2");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(createCPList(ci)).contains(clazz));
		
		InnerClass incl = getInnerClass(clazz.getAttributes(), "org/jasm/test/testclass/Class2$1");
		assertNotNull(incl);
		assertNull(incl.getOuterClassName());
		assertNull(incl.getInnerName());
		
		incl = getInnerClass(clazz.getAttributes(), "org/jasm/test/testclass/Class2$InnerClass");
		assertNotNull(incl);
		assertEquals("org/jasm/test/testclass/Class2", incl.getOuterClassName());
		assertEquals("InnerClass", incl.getInnerNameValue());
		assertTrue(incl.getModifier().isPrivate());
		
		String name = "staticString"; String descriptor = "Ljava/lang/String;";
		assertNotNull(clazz.getFields().getField(name, descriptor));
		List<Utf8Info> utf8 = clazz.getConstantPool().getUtf8Infos("staticString");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(createCPList(utf8)).contains(clazz.getFields().getField(name, descriptor)));
		
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
		assertEquals((getAttributeContent(clazz.getFields().getField(name, descriptor).getAttributes(), ConstantValueAttributeContent.class)).getValue(), new Integer(0));
		
		
		
		name = "constInt"; 
		descriptor = "I";
		assertEquals((getAttributeContent(clazz.getFields().getField(name, descriptor).getAttributes(), ConstantValueAttributeContent.class)).getValue(), new Integer(1));
		
		name = "methodMitException"; 
		descriptor = "()V";
		assertEquals((getAttributeContent(clazz.getMethods().getMethod(name, descriptor).getAttributes(), ExceptionsAttributeContent.class)).getExceptionClassNames().size(), 1);
		assertEquals((getAttributeContent(clazz.getMethods().getMethod(name, descriptor).getAttributes(), ExceptionsAttributeContent.class)).getExceptionClassNames().get(0), "java/lang/IllegalArgumentException");
		
		utf8 = clazz.getConstantPool().getUtf8Infos("methodMitException");
		Assert.assertTrue(clazz.getConstantPool().getReferencingItems(createCPList(utf8)).contains(clazz.getMethods().getMethod(name, descriptor)));
		
		name = "privateMethod"; 
		descriptor = "(I)V";
		assertNotNull(getAttributeContent(clazz.getMethods().getMethod(name, descriptor).getAttributes(), DeprecatedAttributeContent.class));
		
		
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
		
	}
	
	private <T extends AbstractConstantPoolEntry> List<AbstractConstantPoolEntry> createCPList(List<T> input) {
		List<AbstractConstantPoolEntry> result = new ArrayList<>();
		result.addAll(input);
		return result;
	}
	
	private InnerClass getInnerClass(Attributes attrs, String name) {
		for (int i=0;i<attrs.getSize(); i++) {
			if (attrs.get(i).getContent() instanceof InnerClassesAttributeContent) {
				InnerClassesAttributeContent content = (InnerClassesAttributeContent)attrs.get(i).getContent();
				for (int j=0;j<content.getSize();j++) {
					InnerClass incl = content.get(j);
					if (incl.getInnerClassName().equals(name)) {
						return incl;
					}
					
				}
			}
		}
		return null;
	}
	
	private <T extends IAttributeContent> T getAttributeContent(Attributes attrs, Class<T> clazz) {
		for (int i=0;i<attrs.getSize(); i++) {
			if (attrs.get(i).getContent().getClass().equals(clazz)) {
				return (T) attrs.get(i).getContent();
			}
		}
		return null;
	}

}
