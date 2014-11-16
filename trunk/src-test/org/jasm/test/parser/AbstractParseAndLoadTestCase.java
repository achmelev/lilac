package org.jasm.test.parser;

import java.io.InputStream;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;
import org.jasm.parser.AssemblerParser;
import org.junit.Assert;

public abstract class AbstractParseAndLoadTestCase {
	
	protected AssemblerParser parser = null;
	
	private Clazz parse() {
		InputStream inp = this.getClass().getClassLoader().getResourceAsStream("org/jasm/test/parser/"+getDateiName());
		parser = new AssemblerParser();
		Clazz clazz =  parser.parse(inp);
		if (parser.getErrorMessages().size() > 0) {
			parser.debugErrors();
			Assert.fail("Parsing failed!");
		}
		return clazz;
	}
	
	protected abstract String getDateiName();
	protected abstract String getClassName();
	protected abstract void testClass(Class cl);
	
	protected void doTest() {
		Clazz clazz = parse();
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		
		MyClassLoader loader = new MyClassLoader(getClassName(), data2);
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
	
	MyClassLoader(String name, byte[] content) {
		this.name = name;
		this.content = content;
	}
	

	public Class findClass(String name) {
    	byte[] ba = content;
    	return defineClass(name,ba,0,ba.length);
    }
}
