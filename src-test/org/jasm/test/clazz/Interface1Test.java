package org.jasm.test.clazz;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.test.item.DummyRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class Interface1Test {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/Interface1.class");
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
		
		assertEquals(clazz.getThisClass().getClassName(),"org/jasm/test/testclass/Interface1");
		assertEquals(clazz.getSuperClass().getClassName(),"java/lang/Object");
		assertEquals(clazz.getInterfaces().size(), 0);
		assertEquals(clazz.getAttributes().getSize(), 1);
		assertTrue(clazz.getModifier().isPublic());
		assertTrue(clazz.getModifier().isAbstract());
		assertFalse(clazz.getModifier().isAnnotation());
		assertFalse(clazz.getModifier().isEnum());
		assertFalse(clazz.getModifier().isFinal());
		assertTrue(clazz.getModifier().isInterface());
		assertFalse(clazz.getModifier().isSuper());
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
		
	}

}
