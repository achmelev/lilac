package org.jasm.test.errors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.instructions.macros.builtin.BuiltinMacroFactory;
import org.jasm.parser.AssemblerParser;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.test.macro.TestMacroFactory;
import org.junit.Test;

public class MacrosErrorsTest {
	
	
	@Test
	public void testBuiltinMacros() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.parser.BuiltinMacro");
		String originalCode = disassemble(data);
		String code = insert(originalCode, 4, "const classref org/jasm/test/testclasss/BuiltinMacros;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"dublicate"));
		
		code = insert(originalCode, 4, "const classref org/jasm/test/testclasss/BuiltinMacros as BuiltinMacros;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"dublicate"));
		
		code = patch(originalCode, 6, "IBuiltinMacros", "IBuiltinMacross");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 29,"unknown"));
		
		code = patch(originalCode, 49, "Int", "Intt");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 49,"unknown"));
		
		code = patch(originalCode, 56, "Runnable", "Runnablee");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 56,"unknown"));
		
		code = patch(originalCode, 67, "String", "Stringg");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 67,"unknown"));
		
		code = patch(originalCode, 68, ".concat", ".concatt");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 68,"unknown"));
		
		code = patch(originalCode, 68, "this", "thiss");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 68,"unknown"));
		
		code = patch(originalCode, 78, "BuiltinMacros", "BuiltinMacross");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 78,"unknown"));
		
		code = patch(originalCode, 68, "(byte)", "(Runnable)");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 68,"can not cast"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8", "");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong number"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8",
				"this,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong argument type"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8",
				"BuiltinMacros,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong argument type"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8",
				"concat,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong number of arguments"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8",
				"concat,.getfield(this, intArray),.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"can not cast"));
		
		code = patch(originalCode, 73, "concat,this,.invokevirtual(toString,this),(Byte)arg1,(Boolean)arg2,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8",
				"concat,this,.invokevirtual(toString,this),(Byte)arg1,(BuiltinMacros)this,(Char)arg3,(Double)arg4,(Float)arg5,(Int)arg6,(Long)arg7,(Short)arg8");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"can not cast"));
		
		code = patch(originalCode, 92, "IBuiltinMacros.concat", "concat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 92,"wrong method type"));
		
		code = patch(originalCode, 73, "concat", "IBuiltinMacros.concat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong method type"));
		
		code = patch(originalCode, 85, "concat3,", "concat3,this,");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 85,"wrong number"));
		
		
		
	}
	
	
	private  byte [] getData(String name) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		name = name.replace('.', '/')+".jasm";
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
		return new String(data);
	}
	
	private static byte[] assemble(String code, TestErrorsListener listener) {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(out);
		writer.print(code);
		writer.close();
		
		ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
		
		AssemblerParser parser = new AssemblerParser();
		parser.addErrorListener(listener);
		parser.setMacroFactory(new TestMacroFactory());
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
	
	private String remove(String code, int lineNumber) {
		StringReader reader = new StringReader(code);
		LineNumberReader ln = new LineNumberReader(reader);
		StringBuilder result = new StringBuilder();
		try {
			String line = ln.readLine();
			while (line != null) {
				if (ln.getLineNumber() == lineNumber) {
					
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
	
	private String insert(String code, int lineNumber, String newLine) {
		StringReader reader = new StringReader(code);
		LineNumberReader ln = new LineNumberReader(reader);
		StringBuilder result = new StringBuilder();
		try {
			String line = ln.readLine();
			while (line != null) {
				if (ln.getLineNumber() == lineNumber) {
					result.append(newLine+"\n");
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
	
	private boolean checkForErrorMessage(TestErrorsListener listener, int lineNumber, String prefix) {
		List<String> messages = listener.getMessages(lineNumber);
		for (String message: messages) {
			if (message.startsWith(prefix)) {
				return true;
			}
		}
		listener.clear();
		return false;
	}

}
