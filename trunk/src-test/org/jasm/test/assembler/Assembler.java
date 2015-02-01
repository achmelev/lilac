package org.jasm.test.assembler;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;
import org.jasm.loader.AssemblerClassLoaderException;
import org.jasm.parser.AssemblerParser;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.type.verifier.VerifierParams;

public class Assembler {

	public static void main(String[] args) {
		
		try {
			String name = args[0];
			name = name.replace('.', '/')+".jasm";
			
			File outDir = new File(args[1]);
			if (!outDir.exists() || outDir.isFile()) {
				throw new IllegalArgumentException("Illegal dir: "+outDir.getAbsolutePath());
			}
			byte [] data = assemble(getData(name), name);
			String dir = name.substring(0, name.lastIndexOf('/'));
			String fileName = name.substring(name.lastIndexOf('/')+1, name.length());
			fileName = fileName.substring(0, fileName.indexOf('.'))+".class";
			File newDir = new File(outDir,dir);
			File newFile = new File(newDir, fileName);
			newDir.mkdirs();
			FileOutputStream out = new FileOutputStream(newFile);
			out.write(data);
			out.close();
			System.out.println("Written "+newFile.getAbsolutePath());
			
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}
	
	protected static InputStream getData(String name) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream stream = cl.getResourceAsStream(name);
		return stream;
	}
	
	protected static byte[] assemble(InputStream inp,  String rName) {
		AssemblerParser parser = new AssemblerParser();
		Clazz clazz =  parser.parse(inp);
		if (parser.getErrorMessages().size() > 0) {
			parser.printErrors();
			throw new AssemblerClassLoaderException("invalid assembler file", rName, parser.getErrorMessages());
		}
		VerifierParams params = new VerifierParams();
		ClassInfoResolver clp = new ClassInfoResolver();
		clp.add(new ClazzClassPathEntry(clazz));
		clp.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
		clazz.setResolver(clp);
		//clazz.verify(params);
		if (parser.getErrorMessages().size() > 0) {
			parser.printErrors();
			throw new AssemblerClassLoaderException("invalid assembler file", rName, parser.getErrorMessages());
		}
		byte [] data = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		clazz.write(bbuf, 0);
		return data;
	}

}
