package org.jasm.test.jar;

import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URL;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.environment.Environment;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.JarFileClassPathEntry;
import org.jasm.test.assembler.loader.ByteArrayClassLoader;
import org.jasm.type.verifier.VerifierParams;
import org.junit.Assert;

public abstract class AbstractGenerateStackmapJarTestCase extends
		AbstractJarTestCase {
	
	private int counter = 0;
	
	@Override
	protected void testClass(byte[] data, File jarFile, String name) {
		
		Environment.setBooleanValue("asm.forcestackmaps", true);
		String code = disassemble(data);
		byte[] data2 = assemble(code, jarFile);
		String className = name.substring(0, name.length()-".class".length()).replace('/', '.');
		boolean originalWorks = true;
		try {	
			Class cl = new ByteArrayClassLoader(this.getClass().getClassLoader(), className, data).loadClass(className);
			Constructor[] constructors = cl.getDeclaredConstructors();
			for (Constructor c: constructors) {
				if (c.isAccessible() && c.getParameterTypes().length == 0) {
					Object o = c.newInstance();
					break;
				}
			}
		} catch (Throwable e) {
			
			originalWorks = false;
			
		}
		
		if (originalWorks) {
			counter++;
			try {	
				Class cl = new ByteArrayClassLoader(this.getClass().getClassLoader(), className, data2).loadClass(className);
				Constructor[] constructors = cl.getDeclaredConstructors();
				for (Constructor c: constructors) {
					if (c.isAccessible() && c.getParameterTypes().length == 0) {
						Object o = c.newInstance();
						break;
					}
				}
			} catch (Throwable e) {
				if (e instanceof VerifyError) {
					log.error("Error",e);
					log.error("old code: \n"+code);
					String code2 = disassemble(data2);
					log.error("new code: \n"+code2);
					Assert.fail("Testing failed on:");
				} else {
					counter--;
				}
				
			}
		}
		
		Environment.setBooleanValue("asm.forcestackmaps", false);
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
	
	private ClassInfoResolver clp = null;
	
	protected byte [] assemble(String data, File jarFile) {
		
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream pr = new PrintStream(bo);
		pr.print(data);
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		
		AssemblerParser parser = null;
		parser = new AssemblerParser();
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(bi);
		//params.setCheckReferences(false);
		if (clp == null) {
			clp = new ClassInfoResolver();
			clp.add(new JarFileClassPathEntry(jarFile));
			clp.add(new ClassLoaderClasspathEntry(this.getClass().getClassLoader()));
		}
		if (clazz == null) {
			log.error("code: \n"+data);
			if (parser.getErrorCounter() > 0) {
				log.error("code: \n"+data);
				parser.flushErrors();
				Assert.fail("Parsing failed on:");
			}
			Assert.fail("Parsing failed on:");
		} else {
			clazz.setResolver(clp);
			clazz.verify();
			if (parser.getErrorCounter() > 0) {
				log.error("code: \n"+data);
				parser.flushErrors();
				Assert.fail("Parsing failed on:");
			}
		}
		
		
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		return data2;
		
	}

	@Override
	protected void logStatus() {
		if (clp != null) {
			clp.logStatus();
		}
		
	}

	@Override
	protected void doJarTest() {
		super.doJarTest();
		log.info("Really tested "+counter+" classes");
	}
	
	
	
	
	

}


