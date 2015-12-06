package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class ImulMacroParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "Imulmacro.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.ImulMacro";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			ICalculator calc = (ICalculator)cl.newInstance();
			Assert.assertEquals(80000000, calc.calculate());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		
	}

	@Override
	protected boolean readAgain() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		
		
	}

	@Override
	protected boolean verify() {
		return true;
	}
	
	

}
