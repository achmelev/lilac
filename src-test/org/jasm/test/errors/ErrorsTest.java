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
		
		code = insert(originalCode, 2,"version 52.0;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 3,"dublicate"));
		
		code = remove(originalCode, 3);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 1,"missing"));
		
		code = insert(originalCode,3,"name ThisClass;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"dublicate"));	
		
		
		code = patch(originalCode,2, "52.0","60.0");
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
		
		code = insert(originalCode, 4, "extends Object;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"dublicate"));
		
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
		
		code = insert(originalCode, 5, "implements Task;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 6,"dublicate"));
		
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
		Assert.assertTrue(checkForErrorMessage(listener, 77,"unknown"));
		
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
		Assert.assertTrue(checkForErrorMessage(listener, 101,"unknown"));
		
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
		
		code = remove(originalCode, 224);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 223,"missing"));
		
		code = insert(originalCode, 224,"name log_name;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 225,"dublicate"));
		
		code = patch(originalCode, 224,"log_name","log_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"unknown"));
		
		code = patch(originalCode, 224,"log_name","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"wrong"));
		
		code = patch(originalCode, 15,"\"log\"","\"log.log\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 224,"malformed"));
		
		
		code = remove(originalCode, 225);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 223,"missing"));
		
		code = insert(originalCode, 225,"descriptor log_desc;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 226,"dublicate"));
		
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
		
		code = remove(originalCode, 257);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 256,"missing"));
		
		code = insert(originalCode, 257,"name init0_name;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 258,"dublicate"));
		
		code = patch(originalCode, 257,"init0_name","init0_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"unknown"));
		
		code = patch(originalCode, 257,"init0_name","string_200");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"wrong"));
		
		code = patch(originalCode, 31,"<init>","<init>>");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 257,"malformed"));
		
		code = remove(originalCode, 258);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 256,"missing"));
		
		code = insert(originalCode, 258,"descriptor init0_desc;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 259,"dublicate"));
		
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
		
	}
	
	@Test
	public void testEnclosingMethod() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.playground.CalculatorDelegate$1");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 44,"CalculatorDelegate","CalculatorDelegate2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"unknown"));
		
		code = patch(originalCode, 44,"CalculatorDelegate","CalculatorDelegate_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"wrong"));
		
		code = patch(originalCode, 40,"org/jasm/test/playground/CalculatorDelegate","[Lorg/jasm/test/playground/CalculatorDelegate;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"malformed"));
		
		code = patch(originalCode, 44,"nameandtype_34","nameandtype_344");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"unknown"));
		
		code = patch(originalCode, 44,"nameandtype_34","CalculatorDelegate");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"wrong"));
		
		code = patch(originalCode, 32,"(II)I","I");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 44,"wrong"));
		
		
	}
	
	@Test
	public void testSourceAndSignatureFile() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerClassLoader");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode, 200,"source_file_name","source_file_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 200,"unknown"));
		
		code = patch(originalCode, 200,"source_file_name","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 200,"wrong"));
		
		code = patch(originalCode, 260,"signature_name","signature_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 260,"unknown"));
		
		code = patch(originalCode, 260,"signature_name","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 260,"wrong"));
	}
	
	@Test
	public void testAnnotations() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AnnotatedClass");
		String originalCode = disassemble(data);
		
		String code = remove(originalCode, 55);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 54,"missing"));
		
		code = insert(originalCode, 55,"type type_desc;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 56,"dublicate"));
		
		code = patch(originalCode, 55,"type_desc","type_desc2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 55,"unknown"));
		
		code = patch(originalCode, 55,"type_desc","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 55,"wrong"));
		
		code = patch(originalCode, 22,"Lorg/jasm/test/testclass/TestInvisibleAnnotation;","org/jasm/test/testclass/TestInvisibleAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 55,"malformed"));
		
		code = patch(originalCode, 22,"Lorg/jasm/test/testclass/TestInvisibleAnnotation;","[Lorg/jasm/test/testclass/TestInvisibleAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 55,"malformed"));
		
		code = patch(originalCode, 85,"long_33","long_334");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 85,"unknown"));
		
		code = patch(originalCode, 85,"long_33","int_31");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 85,"wrong"));
		
		code = patch(originalCode, 94,"type_desc$3","type_desc$344");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 94,"unknown"));
		
		code = patch(originalCode, 94,"type_desc$3","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 94,"wrong"));
		
		code = patch(originalCode, 43,"Lorg/jasm/test/testclass/NestedAnnotation;","org/jasm/test/testclass/NestedAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 94,"malformed"));
		
		code = patch(originalCode, 43,"Lorg/jasm/test/testclass/NestedAnnotation;","[Lorg/jasm/test/testclass/NestedAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 94,"malformed"));
		
		code = patch(originalCode, 101,"int_31","int_312");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"unknown"));
		
		code = patch(originalCode, 101,"int_31","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 101,"wrong"));
		
		code = remove(originalCode, 129);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 128,"missing"));
		
		code = insert(originalCode, 129,"type type_desc;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 130,"dublicate"));
		
		code = patch(originalCode, 133,"type_desc$0","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 133,"wrong"));
		
		code = remove(originalCode, 136);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 135,"missing"));
		
		code = insert(originalCode, 136,"name utf8_19;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 137,"dublicate"));
		
		code = patch(originalCode, 136,"utf8_19","utf8_192");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 136,"unknown"));
		
		code = patch(originalCode, 136,"utf8_19","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 136,"wrong"));
		
		code = patch(originalCode, 25,"booleanValue","boolean.Value");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 136,"malformed"));
		
		code = patch(originalCode, 60,"utf8_19","utf8_192");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 60,"unknown"));
		
		code = patch(originalCode, 60,"utf8_19","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 60,"wrong"));
		
		code = patch(originalCode, 25,"booleanValue","boolean.Value");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 60,"malformed"));
		
		code = patch(originalCode, 134,"0","100");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 134,"parameter index out of bounds"));
		
		code = remove(originalCode, 170);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 169,"missing"));
		
		code = insert(originalCode, 170,"type type_desc$3;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 171,"dublicate"));
		
		code = patch(originalCode, 170,"type_desc$3","type_desc$344");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"unknown"));
		
		code = patch(originalCode, 170,"type_desc$3","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"wrong"));
		
		code = patch(originalCode, 43,"Lorg/jasm/test/testclass/NestedAnnotation;","org/jasm/test/testclass/NestedAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"malformed"));
		
		code = patch(originalCode, 43,"Lorg/jasm/test/testclass/NestedAnnotation;","[Lorg/jasm/test/testclass/NestedAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"malformed"));
		
		code = patch(originalCode, 73,"type_desc$1","type_desc$1234");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"unknown"));
		
		code = patch(originalCode, 73,"type_desc$1","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong"));
		
		code = patch(originalCode, 32,"Lorg/jasm/test/testclass/Days;","org/jasm/test/testclass/Days;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"malformed"));
		
		code = patch(originalCode, 32,"Lorg/jasm/test/testclass/Days;","[Lorg/jasm/test/testclass/Days;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"malformed"));
		
		code = patch(originalCode, 73,"utf8_47","utf8_4723");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"unknown"));
		
		code = patch(originalCode, 52,"MONDAY","MONDAY.MONDAY");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"malformed"));
		
		code = patch(originalCode, 73,"utf8_47","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 73,"wrong"));
		
		code = patch(originalCode, 77,"type_desc$2","type_desc$234");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 77,"unknown"));
		
		code = patch(originalCode, 77,"type_desc$2","long_33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 77,"wrong"));
		
		code = patch(originalCode, 35,"Ljava/lang/Void;","Ljava/lang/Void");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 77,"malformed"));
		
	}
	
	
	@Test
	public void testTypeAnnotations() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.TypeAnnotationClass");
		String originalCode = disassemble(data);
		
		String code = remove(originalCode, 169);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 168,"missing"));
		
		code = insert(originalCode, 169,"type type_desc$0;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"dublicate"));
		
		code = patch(originalCode, 169,"type type_desc$0","type type_desc$00");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 169,"unknown"));
		
		code = patch(originalCode, 169,"type_desc$0","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 169,"wrong"));
		
		code = patch(originalCode, 18,"Lorg/jasm/test/testclass/EmptyInvisibleTypeAnnotation;","org/jasm/test/testclass/EmptyInvisibleTypeAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 169,"malformed"));
		
		code = patch(originalCode, 18,"Lorg/jasm/test/testclass/EmptyInvisibleTypeAnnotation;","[Lorg/jasm/test/testclass/EmptyInvisibleTypeAnnotation;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 169,"malformed"));
		
		code = remove(originalCode, 170);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 168,"missing"));
		
		code = insert(originalCode, 170,"targets type parameter 1;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 171,"dublicate"));
		
		code = remove(originalCode, 175);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 173,"missing"));
		
		code = insert(originalCode, 175,"targets type parameter 1;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 176,"dublicate"));
		
		code = remove(originalCode, 180);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 178,"missing"));
		
		code = insert(originalCode, 180,"targets type parameter 1;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 181,"dublicate"));
		
		code = remove(originalCode, 185);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 183,"missing"));
		
		code = insert(originalCode, 185,"targets type parameter 1;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 186,"dublicate"));
		
		code = remove(originalCode, 185);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 183,"missing"));
		
		code = insert(originalCode, 185,"targets type parameter 1;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 186,"dublicate"));
		
		code = remove(originalCode, 215);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 213,"missing"));
		
		code = insert(originalCode, 215,"targets field type;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 216,"dublicate"));
		
		code = remove(originalCode, 299);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 297,"missing"));
		
		code = insert(originalCode, 299,"targets catch type try_0;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 300,"dublicate"));
		
		code = patch(originalCode, 170,"implref_1","implref_122");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 170,"unknown"));
		
		code = patch(originalCode, 175,"1","-1");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 175,"parameter index"));
		
		code = patch(originalCode, 180,"1","-1");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 180,"parameter index"));
		
		code = patch(originalCode, 180,"1","300");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 180,"parameter index"));
		
		code = patch(originalCode, 299,"try_0","try_04");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 299,"unknown"));
		
		code = patch(originalCode, 375,"ir1","ir11");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 375,"unknown"));
		
		code = patch(originalCode, 460,"ir3","ir33");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 460,"unknown"));
		
		code = patch(originalCode, 460,"0","-1");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 460,"parameter"));
		
		code = patch(originalCode, 460,"0","300");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 460,"parameter"));
		
		code = patch(originalCode, 493,"ir64","ir645");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 493,"unknown"));
		
		code = patch(originalCode, 493,"ir95","ir955");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 493,"unknown"));
		
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
	
	@Test
	public void testDebugInfos() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,261,"ir4","ir03");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 261,"unknown"));
		
		code = patch(originalCode,261,"25","0");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 261,"line"));
		
		code = patch(originalCode,270,"callback","callback2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"unknown"));
		
		code = patch(originalCode,270,"callback_name","callback_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"unknown"));
		
		code = patch(originalCode,270,"callback_name","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"wrong"));
		
		code = patch(originalCode,21,"\"callback\"","\"callback.callback\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"malformed"));
		
		code = patch(originalCode,270,"callback_desc","callback_desc2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"unknown"));
		
		code = patch(originalCode,270,"callback_desc","ThisClass");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"wrong"));
		
		code = patch(originalCode,22,"Lorg/jasm/tools/task/ITaskCallback;","org/jasm/tools/task/ITaskCallback;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 270,"malformed"));
		
		code = patch(originalCode,380,"ir140->ir254","ir254->ir140");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 380,"illegal"));
	}
	
	@Test
	public void testExceptionHandler() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,398,"IOException","IOException2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"unknown"));
		
		code = patch(originalCode,398,"IOException","IOException_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"wrong"));
		
		code = patch(originalCode,170,"java/io/IOException","[Ljava/io/IOException;");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"malformed"));
		
		code = patch(originalCode,398,"ir265","ir2655");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"unknown"));
		
		code = patch(originalCode,398,"ir254","ir2544");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"unknown"));
		
		code = patch(originalCode,398,"ir259","ir2594");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"unknown"));
		
		code = patch(originalCode,398,"ir254->ir259","ir259->ir254");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 398,"illegal"));
		
	}
	
	@Test
	public void testBootstrapMethod() {
		
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.LambdaExample$FirstLevel");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,115,"LambdaMetafactory.metafactory_handle","LambdaMetafactory.metafactory_handless");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 115,"unknown"));
		
		code = patch(originalCode,115,"LambdaMetafactory.metafactory_handle","methodtype_100");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 115,"wrong"));
		
		code = patch(originalCode,115,"lambda$0_handle","lambda$0_handle2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 115,"unknown"));
		
		code = patch(originalCode,115,"lambda$0_handle","method_desc$0");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 115,"wrong"));
		
	}
	
	@Test
	public void testExternalReferences() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,10,"java/lang/Object","java/langg/Object");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 4,"class"));
		
		code = patch(originalCode,12,"org/jasm/tools/task/Task","org/jasm/tools/taskk/Task");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 5,"class"));
		
		code = patch(originalCode,131,"\"getName\"","\"getNamee\"");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 494,"method"));
		
		code = patch(originalCode,141,"java/lang/Class","java/langg/Class");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 494,"class"));
		
		code = insert(originalCode,23,"const utf8 clazz_name2 \"clazzz\";");
		code = patch(code,51,"clazz_name","clazz_name2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 461,"field"));
		
		
	}
	
	@Test
	public void testInstructions() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,414,"this","this0");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 414,"unknown"));
		
		code = patch(originalCode,414,"this","twoStages");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 414,"wrong"));
		
		code = patch(originalCode,424,"Environment.getBooleanValue","Environment.getBooleanValue2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 424,"unknown"));
		
		code = patch(originalCode,424,"Environment.getBooleanValue","Environment");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 424,"wrong"));
		
		code = patch(originalCode,424,"Environment.getBooleanValue","AssemblerParser.addErrorListener");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 424,"method"));
	}
	
	@Test
	public void testStackmap() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.testclass.AssemblerTask");
		String originalCode = disassemble(data);
		
		String code = patch(originalCode,383,"ir16","ir164");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 383,"unknown"));
		
		code = patch(originalCode,383,"InputStream","InputStream2");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 383,"unknown"));
		
		code = patch(originalCode,383,"InputStream","InputStream_name");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 383,"wrong"));
		
		code = remove(originalCode,385);
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 467,"code verification error"));
	}

	
	@Test
	public void testConstructorAndInvokeSpecial() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.verify.ConstrConcat");
		String originalCode = disassemble(data);
		
		String code = remove(originalCode,69);
		for (int i=0;i<4; i++) {
			code = remove(code,69);
		}
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 69,"code verification error"));
		
		code = patch(originalCode,106,"invokevirtual","invokespecial");
		assemble(code, listener);
		Assert.assertTrue(checkForErrorMessage(listener, 106,"code verification error"));
	}
	
	@Test
	public void testBuiltinMacros() {
		TestErrorsListener listener = new TestErrorsListener();
		byte[] data = getData("org.jasm.test.parser.BuiltinMacro");
		String originalCode = disassemble(data);
		
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
