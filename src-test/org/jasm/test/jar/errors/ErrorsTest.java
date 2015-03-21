package org.jasm.test.jar.errors;

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
import org.jasm.parser.AssemblerParser;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.junit.Test;

public class ErrorsTest {
	
	@Test
	public void testClassStatements() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 1,"public","public interface");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 1,"illegal"));
		
		code = remove(originalCode, 2);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 1,"missing"));
		
		code = patch(originalCode,2, "52_0","60_0");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 2,"illegal"));
		
		code = remove(originalCode, 3);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 1,"missing"));
		
		code = patch(originalCode, 3,"ThisClass","ThisClass2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 3,"unknown"));
		
		code = patch(originalCode, 3,"ThisClass","Object_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 3,"wrong"));
		
		code = patch(originalCode, 8,"org/jasm/test/testclass/AssemblerTask","org.jasm.test.testclass.AssemblerTask");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 3,"unknown"));
		
		code = patch(originalCode, 8,"org/jasm/test/testclass/AssemblerTask","[Lorg/jasm/test/testclass/AssemblerTask;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 3,"malformed"));
		
		code = remove(originalCode, 4);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 1,"missing"));
		
		code = patch(originalCode, 4,"Object","Object2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"unknown"));
		
		code = patch(originalCode, 4,"Object","Object_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"wrong"));
		
		code = patch(originalCode, 10,"java/lang/Object","java.lang.Object");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"unknown"));
		
		code = patch(originalCode, 10,"java/lang/Object","[Ljava/lang/Object;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"malformed"));
		
		code = patch(originalCode, 5,"Task","Task2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"unknown"));
		
		code = patch(originalCode, 5,"Task","Task_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"wrong"));
		
		code = patch(originalCode, 12,"org/jasm/tools/task/Task","org.jasm.tools.task.Task");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"unknown"));
		
		code = patch(originalCode, 12,"org/jasm/tools/task/Task","[Ljava/lang/Object;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"malformed"));
	}
	
	@Test
	public void testConstants() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 11,"Task_name","Task_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 11,"unknown"));
		
		code = patch(originalCode, 12,"org/jasm/tools/task/Task","org.jasm.tools.task.Task");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 11,"malformed"));
		
		code = patch(originalCode, 35,"method_desc","method_desc2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 35,"unknown"));
		
		code = patch(originalCode, 36,"()V","())V");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 35,"malformed"));
		
		code = patch(originalCode, 31,"<init>","<init>>");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 35,"malformed"));
		
		code = patch(originalCode, 50,"clazz_name","clazz_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"unknown"));
		
		code = patch(originalCode, 24,"Lorg/jasm/item/clazz/Clazz;","org/jasm/item/clazz/Clazz;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"malformed"));
		
		code = patch(originalCode, 23,"\"clazz\"","\"clazz@\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"malformed"));
		
		code = patch(originalCode, 34,"Object.init0_nat","Object.init0_nat2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 34,"unknown"));
		
		code = patch(originalCode, 34,"Object.init0_nat","log_nat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 34,"wrong"));
		
		code = patch(originalCode, 85,"Resource.createInputStream_nat","log_nat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 85,"wrong"));
		
		code = patch(originalCode, 8,"org/jasm/test/testclass/AssemblerTask","[Lorg/jasm/test/testclass/AssemblerTask;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 47,"malformed"));
		
		code = patch(originalCode, 77,"utf8_71","Environment");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 77,"wrong"));
		
		code = patch(originalCode, 77,"utf8_71","utf8_711");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 77,"unknown"));
		
	}
	
	@Test
	public void testConstants2() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.LambdaExample$FirstLevel");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 100,"LambdaMetafactory.metafactory","LambdaMetafactory.metafactory2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 100,"unknown"));
		
		code = patch(originalCode, 100,"LambdaMetafactory.metafactory","LambdaMetafactory_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 100,"wrong"));
		
		code = patch(originalCode, 101,"accept_desc$0","accept_desc$0_22");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"unknown"));
		
		code = patch(originalCode, 101,"accept_desc$0","LambdaMetafactory.metafactory");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"wrong"));
		
		code = patch(originalCode, 44,"(Ljava/lang/Object;)V","Ljava/lang/Object;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"malformed"));
		
		code = patch(originalCode, 44,"(Ljava/lang/Object;)V","(Ljava/lang/Object;)VV");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"malformed"));
		
		code = patch(originalCode, 33,"bootstrap_LambdaMetafactory.metafactory","bootstrap_LambdaMetafactory.metafactory2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 33,"unknown"));
		
		code = patch(originalCode, 33,"nameandtype_24","nameandtype_243");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 33,"unknown"));
		
		code = patch(originalCode, 33,"nameandtype_24","LambdaMetafactory.metafactory");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 33,"wrong"));
		
		code = patch(originalCode, 33,"nameandtype_24","x_nat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 33,"wrong"));
		
		code = patch(originalCode, 33,"nameandtype_24","Object.init0_nat");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 33,"illegal"));
		
		
	}
	
	@Test
	public void testFieldStatements() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 227,"private","private public");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 227,"illegal"));
		
		code = patch(originalCode, 224,"log_name","log_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"unknown"));
		
		code = patch(originalCode, 224,"log_name","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"wrong"));
		
		code = patch(originalCode, 15,"\"log\"","\"log.log\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"malformed"));
		
		code = patch(originalCode, 225,"log_desc","log_desc2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 225,"unknown"));
		
		code = patch(originalCode, 225,"log_desc","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 225,"wrong"));
		
		code = patch(originalCode, 16,"Lorg/slf4j/Logger;","org/slf4j/Logger;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 225,"malformed"));
		
		code = patch(originalCode, 16,"Lorg/slf4j/Logger;","(Lorg/slf4j/Logger;)V");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 225,"malformed"));
	}
	
	@Test
	public void testMethodStatements() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 256,"public","protected public");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 256,"illegal"));
		
		code = patch(originalCode, 257,"init0_name","init0_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"unknown"));
		
		code = patch(originalCode, 257,"init0_name","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"wrong"));
		
		code = patch(originalCode, 31,"<init>","<init>>");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"malformed"));
		
		code = patch(originalCode, 258,"init0_desc","init0_desc2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 258,"unknown"));
		
		code = patch(originalCode, 258,"init0_desc","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 258,"wrong"));
		
		code = patch(originalCode, 32,"Lorg/jasm/tools/task/ITaskCallback;Lorg/jasm/tools/resource/Resource;Ljava/util/Properties;)V","Lorg/jasm/tools/task/ITaskCallback;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 258,"malformed"));
	}
	
	@Test
	public void testThrows() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerClassLoader");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 259,"ClassNotFoundException","ClassNotFoundExceptionn");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 259,"unknown"));
		
		code = patch(originalCode, 259,"ClassNotFoundException","ClassNotFoundException_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 259,"wrong"));
		
		code = patch(originalCode, 49,"java/lang/ClassNotFoundException","[Ljava/lang/ClassNotFoundException;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 259,"malformed"));
	}
	
	@Test
	public void testInnerClass() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.LambdaExample");
		String originalCode = disassemble(data);
		
		String code = remove(originalCode, 50);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 49,"missing"));
		
		code = patch(originalCode, 50,"LambdaExample$FirstLevel","LambdaExample$FirstLevell");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"unknown"));
		
		code = patch(originalCode, 50,"LambdaExample$FirstLevel","LambdaExample$FirstLevel_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"wrong"));
		
		code = patch(originalCode, 27,"org/jasm/test/testclass/LambdaExample$FirstLevel","[Lorg/jasm/test/testclass/LambdaExample$FirstLevel;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 50,"malformed"));
		
		code = patch(originalCode, 51,"ThisClass","ThisClasss");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 51,"unknown"));
		
		code = patch(originalCode, 51,"ThisClass","ThisClass_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 51,"wrong"));
		
		code = patch(originalCode, 52,"FirstLevel_name","FirstLevel_namee");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 52,"unknown"));
		
		code = patch(originalCode, 52,"FirstLevel_name","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 52,"wrong"));
		
		code = patch(originalCode, 47,"\"FirstLevel\"","\"org.FirstLevel\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 52,"malformed"));
	}
	
	
	@Test
	public void testAnnotationTargetContext() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.TypeAnnotationClass");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,165,"supertype","field type");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 165, "target type illegal"));
		
		listener.clear();
		code = patch(originalCode,215,"field type","supertype");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 215, "target type illegal"));
		
		listener.clear();
		code = patch(originalCode,299,"catch type try_0","field type");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 299, "target type illegal"));
		
		
	}
	
	@Test
	public void testAnnotationDescriptor() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.TypeAnnotationClass");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,20,"Lorg/jasm/test/testclass/EmptyVisibleTypeAnnotation;","org/jasm/test/testclass/EmptyVisibleTypeAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 184,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 189,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 194,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 199,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 211,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 219,"malformed type descriptor"));
		Assert.assertTrue(checkForErrorMessage(listener, 303,"malformed type descriptor"));
		
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
