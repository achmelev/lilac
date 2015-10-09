package org.jasm.test.jar.maven;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public abstract class AbstractAssembleDisassembleMavenJarTestCase extends
		AbstractMavenJarTestCase {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void testClass(byte[] data, String name) {
		
		String code = disassemble(data);
		byte[] data2 = assemble(code);
		assertArrayEquals(data, data2);
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
	
	private ClassInfoResolver classPath = null;
	
	protected byte [] assemble(String data) {
		
		//log.error("code: \n"+data);
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream pr = new PrintStream(bo);
		pr.print(data);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		
		AssemblerParser parser = null;
		parser = new AssemblerParser();
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(bi);
		if (parser.getErrorCounter() > 0) {
			log.error("code: \n"+data);
			parser.flushErrors();
			Assert.fail("Parsing failed");
		}
		if (classPath == null) {
			classPath = new ClassInfoResolver();
			classPath.add(getRootEntry());
			classPath.add(new ClassLoaderClasspathEntry(this.getClass().getClassLoader()));
			for (MavenJarClassPathEntry entry: getDependencies()) {
				classPath.add(entry);
			}
		}
		clazz.setResolver(classPath);
		clazz.verify();
		if (parser.getErrorCounter() > 0) {
			log.error("code: \n"+data);
			parser.flushErrors();
			Assert.fail("Parsing failed");
		}
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		return data2;
		
	}
	
	@Override
	protected boolean filter(String name) {
		return true;
	}

}
