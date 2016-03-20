package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
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
		return true;
	}
	

	@Override
	protected boolean verify() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		Method m = cl.getMethods().getMethod("calculate", "()I");
		Assert.assertEquals(2, ((CodeAttributeContent)m.getAttributes().
				getAttributesByContentType(CodeAttributeContent.class).get(0).getContent()).getMaxStack());
	}
	
	
	

}
