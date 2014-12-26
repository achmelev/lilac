package org.jasm.loader;

import java.io.InputStream;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.classpath.ClassLoaderClasspathEntry;
import org.jasm.item.classpath.ClassPath;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.jasm.type.verifier.VerifierParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerClassLoader extends ClassLoader {
	
	private boolean parentFirst = false;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	
	public AssemblerClassLoader() {
		super();
	}

	public AssemblerClassLoader(ClassLoader parent) {
		super(parent);
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
		Clazz clazz =  parser.parse(inp);
		VerifierParams params = new VerifierParams();
		ClassPath clp = new ClassPath();
		clp.add(new ClassLoaderClasspathEntry(this));
		clazz.setClasspath(clp);
		clazz.verify(params);
		if (parser.getErrorMessages().size() > 0) {
			parser.debugErrors();
			throw new AssemblerClassLoaderException("invalid assembler file", rName, parser.getErrorMessages());
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
