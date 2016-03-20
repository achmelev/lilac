package org.jasm.test.parser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.IBuiltinMacros;
import org.jasm.test.testclass.MyRunnable;
import org.jasm.test.testclass.TestBean;
import org.junit.Test;

//@Ignore
public class HelloWorldMacroParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "HelloWorldMacro.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.HelloWorld";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			
			
					
		} catch (Exception e) {
			
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	protected boolean readAgain() {
		return true;
	}

	@Override
	protected boolean verify() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		
		
	}
	
	

}
