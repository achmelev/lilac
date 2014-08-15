package org.jasm.test.clazz;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.attribute.Annotation;
import org.jasm.item.attribute.AnnotationElementNameValue;
import org.jasm.item.attribute.AnnotationElementValue;
import org.jasm.item.attribute.RuntimeInvisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleParameterAnnotationsAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.test.item.DummyRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class AnnotatedClassTest {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/AnnotatedClass.class");
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
		
		assertTrue(clazz.getAttributes().get(1).getContent() instanceof RuntimeInvisibleAnnotationsAttributeContent);
		RuntimeVisibleAnnotationsAttributeContent content = (RuntimeVisibleAnnotationsAttributeContent)clazz.getAttributes().get(2).getContent();
		Annotation ann = content.get(0);
		
		assertEquals("Lorg/jasm/test/testclass/TestAnnotation;",ann.getTypeValue());
		assertEquals(9, ann.getValues().size());
		
		AnnotationElementNameValue anmv = ann.get(0);
		assertEquals("booleanValue", anmv.getNameValue());
		assertEquals(0, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(1);
		assertEquals("byteValue", anmv.getNameValue());
		assertEquals(1, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(2);
		assertEquals("charValue", anmv.getNameValue());
		assertEquals(2, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(3);
		assertEquals("clazzValue", anmv.getNameValue());
		assertEquals("Ljava/lang/Void;", anmv.getValue().getClassName());
		
		anmv = ann.get(4);
		assertEquals("intValue", anmv.getNameValue());
		assertEquals(5, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(5);
		assertEquals("longValue", anmv.getNameValue());
		assertEquals(new Long(6), anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(6);
		assertEquals("shortValue", anmv.getNameValue());
		assertEquals(7, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(7);
		assertEquals("Lorg/jasm/test/testclass/NestedAnnotation;", anmv.getValue().getNestedAnnotation().getTypeValue());
		
		anmv = ann.get(8);
		AnnotationElementValue[] array =  anmv.getValue().getArrayMembers();
		assertEquals(3, array.length);
		assertEquals(2,array[0].getPrimitiveValue());
		assertEquals(5,array[1].getPrimitiveValue());
		assertEquals(6,array[2].getPrimitiveValue());
		
		
		assertTrue(clazz.getMethods().getMethod("annotatedMethod", "(I)V").getAttributes().get(0).getContent() instanceof RuntimeInvisibleParameterAnnotationsAttributeContent);
		RuntimeVisibleParameterAnnotationsAttributeContent parContent =  (RuntimeVisibleParameterAnnotationsAttributeContent)clazz.getMethods().getMethod("annotatedMethod", "(I)V").getAttributes().get(1).getContent();
		ann = parContent.get(0).get(0);
		
		assertEquals("Lorg/jasm/test/testclass/TestAnnotation;",ann.getTypeValue());
		assertEquals(9, ann.getValues().size());
		
		anmv = ann.get(0);
		assertEquals("booleanValue", anmv.getNameValue());
		assertEquals(0, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(1);
		assertEquals("byteValue", anmv.getNameValue());
		assertEquals(1, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(2);
		assertEquals("charValue", anmv.getNameValue());
		assertEquals(2, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(3);
		assertEquals("clazzValue", anmv.getNameValue());
		assertEquals("Ljava/lang/Void;", anmv.getValue().getClassName());
		
		anmv = ann.get(4);
		assertEquals("intValue", anmv.getNameValue());
		assertEquals(5, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(5);
		assertEquals("longValue", anmv.getNameValue());
		assertEquals(new Long(6), anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(6);
		assertEquals("shortValue", anmv.getNameValue());
		assertEquals(7, anmv.getValue().getPrimitiveValue());
		
		anmv = ann.get(7);
		assertEquals("Lorg/jasm/test/testclass/NestedAnnotation;", anmv.getValue().getNestedAnnotation().getTypeValue());
		
		anmv = ann.get(8);
		array =  anmv.getValue().getArrayMembers();
		assertEquals(3, array.length);
		assertEquals(2,array[0].getPrimitiveValue());
		assertEquals(5,array[1].getPrimitiveValue());
		assertEquals(6,array[2].getPrimitiveValue());
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
		
	}

}