package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class MaxStackParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "MaxStack.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.MaxStack";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			ICalculator calc = (ICalculator)cl.newInstance();
			Assert.assertEquals(100, calc.calculate());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	protected boolean readAgain() {
		return false;
	}
	
	

}
