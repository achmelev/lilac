package org.jasm.test.parser;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.loader.AssemblerClassLoaderException;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.test.macro.TestMacroFactory;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractParseAndLoadTestCase {
	
	protected AssemblerParser parser = null;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Clazz parse() {
		InputStream inp = this.getClass().getClassLoader().getResourceAsStream("org/jasm/test/parser/"+getDateiName());
		parser = new AssemblerParser();
		parser.setMacroFactory(new TestMacroFactory());
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(inp);
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();
			Assert.fail("Parsing failed!");
		} else {
		
			if (verify()) {
				ClassInfoResolver clp = new ClassInfoResolver();
				clp.add(new ClazzClassPathEntry(clazz));
				clp.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
				clazz.setResolver(clp);
				clazz.verify();
				if (parser.getErrorCounter()>0) {
					parser.flushErrors();;
					if (!readAgain()) {
						Assert.fail("class verifying failed!");
					}	
				}
			}
		}	
		return clazz;
	}
	
	private Clazz parse(String s) {
		Reader inp = new StringReader(s);
		parser = new AssemblerParser();
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(inp);
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();;
			Assert.fail("Parsing disassembled failed!");
		} else {
		
			if (verify()) {
				ClassInfoResolver clp = new ClassInfoResolver();
				clp.add(new ClazzClassPathEntry(clazz));
				clp.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
				clazz.setResolver(clp);
				clazz.verify();
				if (parser.getErrorCounter()>0) {
					parser.flushErrors();;
					Assert.fail("class verifying failed!");
				}
			}
		}	
		
		return clazz;
	}
	
	protected abstract String getDateiName();
	protected abstract String getClassName();
	protected abstract void testClass(Class cl);
	protected abstract void testReadAraginClass(Clazz cl);
	
	protected boolean readAgain() {
		return false;
	}
	
	protected boolean verify() {
		return false;
	}
	
	protected void doTest() {
		Clazz clazz = parse();
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		
		if (readAgain()) {
			clazz = new Clazz();
			clazz.read(bbuf2, 0L);
			clazz.resolve();
			clazz.updateMetadata();
			testReadAraginClass(clazz);
			
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			PrettyPrinter printer = new PrettyPrinter(writer);
			printer.printItem(clazz);
			writer.close();
			log.debug("code: \n"+sw.toString());
			clazz = parse(sw.toString());
			
			
		}
		
		MyClassLoader loader = new MyClassLoader(getClassName(), data2, this.getClass().getClassLoader());
		try {
			Class cl = loader.loadClass(getClassName());
			testClass(cl);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}

class MyClassLoader extends ClassLoader {
	
	private String name = null;
	private byte [] content;
	
	MyClassLoader(String name, byte[] content, ClassLoader parent) {
		super(parent);
		this.name = name;
		this.content = content;
	}
	
	@Override
	public Class findClass(String name) throws ClassNotFoundException {
		if (!this.name.equals(name)) {
			throw new ClassNotFoundException(name);
		}
    	byte[] ba = content;
    	return defineClass(name,ba,0,ba.length);
    }
}
