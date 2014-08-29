package org.jasm.test.constantpool;

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

import static org.junit.Assert.*;

public class ConstantPoolTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/Class1.class");
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
		
		ConstantPool pool = new ConstantPool();
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		pool.read(bbuf, 8);
		
		pool.setParent(new Clazz());
		pool.resolve();
		pool.updateMetadata();
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		PrettyPrinter printer = new PrettyPrinter(writer);
		printer.printItem(pool);
		writer.close();
		log.debug("code: \n"+sw.toString());
		
		byte [] data1 = new byte[pool.getLength()];
		byte [] data2 = new byte[pool.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		System.arraycopy(data, 8, data1, 0, data1.length);
		pool.write(bbuf2, 0);
		
		assertArrayEquals(data1, data2);
		
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getUtf8Infos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getUtf8Infos("org/jasm/test/testclass/Class1").contains(pool.getClassInfos("org/jasm/test/testclass/Class1").get(0).getClassNameReference()));
		
		assertTrue(pool.getStringEntries("HELLO WORLD").size()>0);
		assertTrue(pool.getUtf8Infos("HELLO WORLD").size()>0);
		assertTrue(pool.getUtf8Infos("HELLO WORLD").contains(pool.getStringEntries("HELLO WORLD").get(0).getUtf8Reference()));
		
		assertTrue(pool.getMethodRefs("java/lang/Object", "<init>", "()V").size()>0);
		assertTrue(pool.getClassInfos("java/lang/Object").size()>0);
		assertTrue(pool.getNameAndTypeInfos("<init>", "()V").size()>0);
		assertTrue(pool.getNameAndTypeInfos("<init>", "()V").contains(pool.getMethodRefs("java/lang/Object", "<init>", "()V").get(0).getNameAndTypeReference()));
		assertTrue(pool.getClassInfos("java/lang/Object").contains(pool.getMethodRefs("java/lang/Object", "<init>", "()V").get(0).getClassReference()));
		
		assertTrue(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").size()>0);
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getNameAndTypeInfos("STRING_CONSTANT", "Ljava/lang/String;").size()>0);
		assertTrue(pool.getNameAndTypeInfos("STRING_CONSTANT", "Ljava/lang/String;").contains(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").get(0).getNameAndTypeReference()));
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").contains(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").get(0).getClassReference()));
		
		assertTrue(pool.getLongEntries(10000).size()>0);
		assertTrue(pool.getFloatEntries(10.1f).size()>0);
		assertTrue(pool.getDoubleEntries(100.1).size()>0);
	}
	
	

}
