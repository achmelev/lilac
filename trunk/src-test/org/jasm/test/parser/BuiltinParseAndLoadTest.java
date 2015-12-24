package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.IBuiltinMacros;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class BuiltinParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "BuiltinMacro.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.BuiltinMacros";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			IBuiltinMacros instance = (IBuiltinMacros)cl.newInstance();
			String result = instance.concat("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertEquals("Anfang MyString: 1, true, X, 1.0, 1.5, 10, 100, 25", result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	protected boolean readAgain() {
		return false;
	}

	@Override
	protected boolean verify() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		
		
	}
	
	

}
