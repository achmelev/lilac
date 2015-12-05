package org.jasm.test.parser;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.IArrayCreator;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

import sun.net.NetHooks;

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
			
			Assert.assertEquals(24, result.length);
			Assert.assertNull(result[0]);
			Assert.assertNull(result[1]);
			Assert.assertEquals(new Integer(10), result[2]);
			Assert.assertEquals(new Integer(-300), result[3]);
			Assert.assertEquals(new Integer(123456), result[4]);
			Assert.assertEquals(new Long(3000000000L), result[5]);
			Assert.assertEquals(new Long(4000000001L), result[6]);
			Assert.assertEquals(new Long(4000000001L), result[7]);
			Assert.assertEquals(new Float(4.4), result[8]);
			Assert.assertEquals(new Float(5.55), result[9]);
			Assert.assertEquals(new Float(5.55), result[10]);
			Assert.assertEquals(new Double(6.66), result[11]);
			Assert.assertEquals(new Double(6.66), result[12]);
			Assert.assertEquals(new Integer(123456), result[13]);
			Assert.assertEquals(new Long(4000000001L), result[14]);
			Assert.assertEquals(new Float(5.55), result[15]);
			Assert.assertEquals(new Double(6.66), result[16]);
			Assert.assertEquals("HelloWorld", result[17]);
			Assert.assertEquals("HelloWorld", result[18]);
			Assert.assertEquals("HelloWorld", result[19]);
			Assert.assertEquals("HelloWorld", result[20]);
			Assert.assertEquals(Object.class, result[21]);
			Assert.assertTrue(result[22] instanceof MethodHandle);
			Assert.assertTrue(((MethodType)result[23]).toMethodDescriptorString().equals("()V"));
			
			
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
		// TODO Auto-generated method stub
		
	}
	

}
