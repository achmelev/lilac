package org.jasm.test.testclass;

import java.io.InputStream;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;
import org.jasm.loader.AssemblerClassLoaderException;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.type.verifier.VerifierParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerClassLoader extends ClassLoader {
	
	private boolean parentFirst = false;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	private boolean verify = false;
	
	public AssemblerClassLoader(ClassLoader parent, boolean verify) {
		super(parent);
		this.verify = verify;
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if (parentFirst) {
			try {
				super.loadClass(name, resolve);
			} catch (ClassNotFoundException e) {
				//ignore
			}
		} 
		byte [] data = tryFindClass(name);
		if (data != null) {
			Class result = defineClass(name,data,0,data.length);
			if (resolve) {
				super.resolveClass(result);
			}
			return result;
		} else {
			if (!parentFirst) {
				return super.loadClass(name, resolve);
			} else {
				throw new ClassNotFoundException(name);
			}
		}
		
		
	}
	
	private byte [] tryFindClass(String name) throws ClassNotFoundException {
		
		String rName = name.replace('.', '/')+".jasm";
		
		InputStream inp = super.getResourceAsStream(rName);
		if (inp == null) {
			return null;
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Loading from "+rName);
		}
		
		AssemblerParser parser = new AssemblerParser();
		parser.addErrorListener(new SimpleParserErrorListener());
		Clazz clazz =  parser.parse(inp);
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();
			throw new AssemblerClassLoaderException("invalid assembler file", rName);
		}
		ClassInfoResolver clp = new ClassInfoResolver();
		clp.add(new ClazzClassPathEntry(clazz));
		clp.add(new ClassLoaderClasspathEntry(this));
		
		clazz.setResolver(clp);
		
		if (verify) {
			clazz.verify();
		}
		
		if (parser.getErrorCounter() > 0) {
			parser.flushErrors();;
			throw new AssemblerClassLoaderException("invalid assembler file", rName);
		}
		byte [] data = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		clazz.write(bbuf, 0);
		return data;
	}

	public void setParentFirst(boolean parentFirst) {
		this.parentFirst = parentFirst;
	}
	
	

}
