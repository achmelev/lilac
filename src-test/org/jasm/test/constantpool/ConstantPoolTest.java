package org.jasm.test.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
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
		
		pool.setParent(new DummyRoot());
		pool.resolve();
		
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
		
		assertNotNull(pool.getClassInfo("org/jasm/test/testclass/Class1"));
		assertNotNull(pool.getUtf8Info("org/jasm/test/testclass/Class1"));
		assertSame(pool.getClassInfo("org/jasm/test/testclass/Class1").getClassNameReference(), pool.getUtf8Info("org/jasm/test/testclass/Class1"));
		
		assertNotNull(pool.getStringEntry("HELLO WORLD"));
		assertNotNull(pool.getUtf8Info("HELLO WORLD"));
		assertSame(pool.getStringEntry("HELLO WORLD").getUtf8Reference(), pool.getUtf8Info("HELLO WORLD"));
		
		assertNotNull(pool.getMethodRef("java/lang/Object", "<init>", "()V"));
		assertNotNull(pool.getClassInfo("java/lang/Object"));
		assertNotNull(pool.getNameAndTypeInfo("<init>", "()V"));
		assertSame(pool.getMethodRef("java/lang/Object", "<init>", "()V").getNameAndTypeReference(),pool.getNameAndTypeInfo("<init>", "()V"));
		assertSame(pool.getMethodRef("java/lang/Object", "<init>", "()V").getClassReference(),pool.getClassInfo("java/lang/Object"));
		
		assertNotNull(pool.getFieldRef("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String"));
		assertNotNull(pool.getClassInfo("org/jasm/test/testclass/Class1"));
		assertNotNull(pool.getNameAndTypeInfo("STRING_CONSTANT", "Ljava/lang/String"));
		assertSame(pool.getFieldRef("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String").getNameAndTypeReference(),pool.getNameAndTypeInfo("STRING_CONSTANT", "Ljava/lang/String"));
		assertSame(pool.getFieldRef("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String").getClassReference(),pool.getClassInfo("org/jasm/test/testclass/Class1"));
		
		assertNotNull(pool.getLongEntry(10000));
		assertNotNull(pool.getFloatEntry(10.1f));
		assertNotNull(pool.getDoubleEntry(100.1));
	}
	
	

}
