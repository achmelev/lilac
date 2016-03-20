package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class InvokeDynamicParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "InvokeDynamic.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.InvokeDynamicClassT";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			ICalculator calc = (ICalculator)cl.newInstance();
			Assert.assertEquals(80, calc.calculate());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	protected boolean readAgain() {
		return false;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		// TODO Auto-generated method stub
		
	}
	
	

}
