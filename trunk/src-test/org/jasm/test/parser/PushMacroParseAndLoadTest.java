package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.IArrayCreator;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class PushMacroParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "PushMacro.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.PushArrayCreator";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			IArrayCreator calc = (IArrayCreator)cl.newInstance();
			Object[] result = calc.createArray();
			
			Assert.assertEquals(2, result.length);
			Assert.assertNull(result[0]);
			Assert.assertNull(result[1]);
			
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
		return false;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		// TODO Auto-generated method stub
		
	}
	

}
