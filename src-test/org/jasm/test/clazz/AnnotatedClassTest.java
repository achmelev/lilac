package org.jasm.test.clazz;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.attribute.Annotation;
import org.jasm.item.attribute.AnnotationElementNameValue;
import org.jasm.item.attribute.AnnotationElementValue;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.attribute.RuntimeInvisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleParameterAnnotationsAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		
		RuntimeInvisibleAnnotationsAttributeContent invisibleAnn = getAttributeContent(clazz.getAttributes(), RuntimeInvisibleAnnotationsAttributeContent.class);
		RuntimeVisibleAnnotationsAttributeContent visibleAnn = getAttributeContent(clazz.getAttributes(), RuntimeVisibleAnnotationsAttributeContent.class);
		
		assertNotNull(invisibleAnn);
		assertNotNull(visibleAnn);
		RuntimeVisibleAnnotationsAttributeContent content = visibleAnn;
		Annotation ann = content.get(0);
		
		assertEquals("Lorg/jasm/test/testclass/TestAnnotation;",ann.getTypeValue());
		assertEquals(10, ann.getValues().size());
		
		AnnotationElementNameValue anmv = (AnnotationElementNameValue)ann.get(0);
		assertEquals("booleanValue", anmv.getNameValue());
		assertEquals(0, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(1);
		assertEquals("byteValue", anmv.getNameValue());
		assertEquals(1, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(2);
		assertEquals("charValue", anmv.getNameValue());
		assertEquals(2, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(3);
		assertEquals("enumValue", anmv.getNameValue());
		assertEquals("MONDAY", anmv.getValue().getEnumConstNameValue());
		assertEquals("Lorg/jasm/test/testclass/Days;", anmv.getValue().getEnumTypeNameValue());
		
		anmv = (AnnotationElementNameValue)ann.get(4);
		assertEquals("clazzValue", anmv.getNameValue());
		assertEquals("Ljava/lang/Void;", anmv.getValue().getClassName());
		
		anmv = (AnnotationElementNameValue)ann.get(5);
		assertEquals("intValue", anmv.getNameValue());
		assertEquals(5, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(6);
		assertEquals("longValue", anmv.getNameValue());
		assertEquals(new Long(6), anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(7);
		assertEquals("shortValue", anmv.getNameValue());
		assertEquals(7, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(8);
		assertEquals("Lorg/jasm/test/testclass/NestedAnnotation;", anmv.getValue().getNestedAnnotation().getTypeValue());
		
		anmv = (AnnotationElementNameValue)ann.get(9);
		List<AnnotationElementValue> array =  anmv.getValue().getArrayMembers();
		assertEquals(3, array.size());
		assertEquals(2,array.get(0).getPrimitiveValue());
		assertEquals(5,array.get(1).getPrimitiveValue());
		assertEquals(6,array.get(2).getPrimitiveValue());
		
		
		RuntimeInvisibleParameterAnnotationsAttributeContent invisibleParAnn = getAttributeContent(clazz.getMethods().getMethod("annotatedMethod", "(I)V").getAttributes(), RuntimeInvisibleParameterAnnotationsAttributeContent.class);
		RuntimeVisibleParameterAnnotationsAttributeContent visibleParAnn = getAttributeContent(clazz.getMethods().getMethod("annotatedMethod", "(I)V").getAttributes(), RuntimeVisibleParameterAnnotationsAttributeContent.class);
		
		assertNotNull(invisibleParAnn);
		assertNotNull(visibleParAnn);
		
		RuntimeVisibleParameterAnnotationsAttributeContent parContent =  visibleParAnn;
		ann = parContent.get(0).get(0);
		
		assertEquals("Lorg/jasm/test/testclass/TestAnnotation;",ann.getTypeValue());
		assertEquals(10, ann.getValues().size());
		
		anmv = (AnnotationElementNameValue)ann.get(0);
		assertEquals("booleanValue", anmv.getNameValue());
		assertEquals(0, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(1);
		assertEquals("byteValue", anmv.getNameValue());
		assertEquals(1, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(2);
		assertEquals("charValue", anmv.getNameValue());
		assertEquals(2, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(3);
		assertEquals("enumValue", anmv.getNameValue());
		assertEquals("TUESDAY", anmv.getValue().getEnumConstNameValue());
		assertEquals("Lorg/jasm/test/testclass/Days;", anmv.getValue().getEnumTypeNameValue());
		
		anmv = (AnnotationElementNameValue)ann.get(4);
		assertEquals("clazzValue", anmv.getNameValue());
		assertEquals("Ljava/lang/Void;", anmv.getValue().getClassName());
		
		anmv = (AnnotationElementNameValue)ann.get(5);
		assertEquals("intValue", anmv.getNameValue());
		assertEquals(5, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(6);
		assertEquals("longValue", anmv.getNameValue());
		assertEquals(new Long(6), anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(7);
		assertEquals("shortValue", anmv.getNameValue());
		assertEquals(7, anmv.getValue().getPrimitiveValue());
		
		anmv = (AnnotationElementNameValue)ann.get(8);
		assertEquals("Lorg/jasm/test/testclass/NestedAnnotation;", anmv.getValue().getNestedAnnotation().getTypeValue());
		
		anmv = (AnnotationElementNameValue)ann.get(9);
		array =  anmv.getValue().getArrayMembers();
		assertEquals(3, array.size());
		assertEquals(2,array.get(0).getPrimitiveValue());
		assertEquals(5,array.get(1).getPrimitiveValue());
		assertEquals(6,array.get(2).getPrimitiveValue());
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
		
	}
	
	private <T> T getAttributeContent(Attributes attrs, Class<T> clazz) {
		for (int i=0;i<attrs.getSize(); i++) {
			if (attrs.get(i).getContent().getClass().equals(clazz)) {
				return (T)attrs.get(i).getContent();
			}
		}
		return null;
		
	}

}
