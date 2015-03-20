package org.jasm.test.jar.errors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.junit.Test;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class ErrorsTest {
	
	
	@Test
	public void testAnnotationTargetContext() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.TypeAnnotationClass");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,165,"supertype","field type");
		assemble(code, listener);
		Assert.assertTrue(listener.getMessages(165).contains("target type illegal in this context"));
		
		listener.clear();
		code = patch(originalCode,215,"field type","supertype");
		assemble(code, listener);
		Assert.assertTrue(listener.getMessages(215).contains("target type illegal in this context"));
		
		listener.clear();
		code = patch(originalCode,299,"catch type try_0","field type");
		assemble(code, listener);
		Assert.assertTrue(listener.getMessages(299).contains("target type illegal in this context"));
		
		
	}
	
	@Test
	public void testAnnotationDescriptor() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.TypeAnnotationClass");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,20,"Lorg/jasm/test/testclass/EmptyVisibleTypeAnnotation;","org/jasm/test/testclass/EmptyVisibleTypeAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(listener.getMessages(184).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(189).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(194).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(199).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(211).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(219).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		Assert.assertTrue(listener.getMessages(303).contains("malformed type descriptor "+"org/jasm/test/testclass/EmptyVisibleTypeAnnotation;"));
		
	}
	
	
	private  byte [] getData(String name) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		name = name.replace('.', '/')+".class";
		InputStream stream = cl.getResourceAsStream(name);
		byte[] data;
		try {
			data = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return data;
	}
	
	private static String disassemble(byte [] data) {
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
		
		return sw.toString();
	}
	
	private static byte[] assemble(String code, TestErrorsListener listener) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(out);
		writer.print(code);
		writer.close();
		
		ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
		
		AssemblerParser parser = new AssemblerParser();
		parser.addErrorListener(listener);
		Clazz clazz =  parser.parse(input);
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();
			return null;
		}
		ClassInfoResolver clp = new ClassInfoResolver();
		clp.add(new ClazzClassPathEntry(clazz));
		clp.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
		clazz.setResolver(clp);
		clazz.verify();
		if (parser.getErrorCounter()>0) {
			parser.flushErrors();;
			return null;
		}
		byte [] data = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		clazz.write(bbuf, 0);
		return data;
	}
	
	private String patch(String code, int lineNumber, String toReplace, String value) {
		StringReader reader = new StringReader(code);
		LineNumberReader ln = new LineNumberReader(reader);
		StringBuilder result = new StringBuilder();
		try {
			String line = ln.readLine();
			while (line != null) {
				if (ln.getLineNumber() == lineNumber) {
					line = line.replace(toReplace, value);
					result.append(line+"\n");
				} else {
					result.append(line+"\n");
				}
				line = ln.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return result.toString();
	}

}
