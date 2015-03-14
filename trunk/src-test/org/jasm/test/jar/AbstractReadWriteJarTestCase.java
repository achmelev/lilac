package org.jasm.test.jar;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.JarFileClassPathEntry;
import org.jasm.type.verifier.VerifierParams;
import org.junit.Assert;

public abstract class AbstractReadWriteJarTestCase extends
		AbstractJarTestCase {
	
	@Override
	protected void testClass(byte[] data, File jarFile, String name) {
		
		byte[] data2 = readWrite(data);
		assertArrayEquals(data, data2);
	}
	
	protected byte[] readWrite(byte [] data) {
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		
		Clazz clazz = new Clazz();
		clazz.read(bbuf, 0L);
		clazz.resolve();
		clazz.updateMetadata();
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		
		return data2;
	}
	
	
	
	
	
	
	
	

}
