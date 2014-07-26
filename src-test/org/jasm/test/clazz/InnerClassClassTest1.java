package org.jasm.test.clazz;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;



import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.attribute.ConstantValueAttributeContent;
import org.jasm.item.attribute.EnclosingMethodAttributeContent;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.test.item.DummyRoot;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class InnerClassClassTest1 {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/Class2$1.class");
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
		log.debug(data.length+":"+clazz.getLength());
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		PrettyPrinter printer = new PrettyPrinter(writer);
		printer.printItem(clazz);
		writer.close();
		log.debug("code: \n"+sw.toString());
		
		
		Assert.assertEquals("org/jasm/test/testclass/Class2", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getClassName());
		Assert.assertEquals("methodWithAnonymousClass", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getMethodName());
		Assert.assertEquals("()V", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getMethodDescriptor());
		
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		
		
		assertArrayEquals(data2, data);
		
	}

}
