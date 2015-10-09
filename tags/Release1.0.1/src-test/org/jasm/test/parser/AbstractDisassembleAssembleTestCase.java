package org.jasm.test.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.type.verifier.VerifierParams;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDisassembleAssembleTestCase {
	
	 private Logger log = LoggerFactory.getLogger(this.getClass());
	
	protected AssemblerParser parser = null;
	
	protected byte [] getOriginalData() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream(getClassResourceName());
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
		return data;
	}
	
	protected String disassemble(byte [] data) {
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
	
	protected byte [] assemble(String data) {
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream pr = new PrintStream(bo);
		pr.print(data);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		
		parser = new AssemblerParser();
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(bi);
		ClassInfoResolver path = new ClassInfoResolver();
		path.add(new ClassLoaderClasspathEntry(this.getClass().getClassLoader()));
		clazz.setResolver(path);
		clazz.verify();
		
		if (parser.getErrorCounter() > 0) {
			log.error("code:\n "+data);
			parser.flushErrors();
			Assert.fail("Parsing failed!");
		}
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		return data2;
		
	}
	
	protected abstract String getClassResourceName();
	
	protected void doTest() {
		byte [] data1 = getOriginalData();
		String code = disassemble(data1);
		log.debug("code: \n"+code);
		byte[] data2 = assemble(code);
		Assert.assertArrayEquals(data1, data2);
	}

}
