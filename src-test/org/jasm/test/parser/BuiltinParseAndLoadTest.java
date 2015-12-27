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
			Assert.assertEquals(new Boolean(true), instance.box(true));
			Assert.assertEquals(new Integer(1), instance.boxZ2I(true));
			Assert.assertEquals(true, instance.unbox(Boolean.TRUE));
			Assert.assertEquals(1, instance.unboxZ2I(Boolean.TRUE));
			
			//Promitive casts
			Assert.assertEquals(1, instance.pconvert1(true));
			Assert.assertEquals(1, instance.pconvert2((char)1));
			Assert.assertEquals(1, instance.pconvert3(1.0));
			Assert.assertEquals(1, instance.pconvert4(1.0f));
			Assert.assertEquals(1, instance.pconvert5(1));
			Assert.assertEquals(1, instance.pconvert6(1L));
			Assert.assertEquals(1, instance.pconvert7((short)1));
			
			Assert.assertEquals(false, instance.pconvert8((byte)0));
			Assert.assertEquals(true, instance.pconvert9((' ')));
			Assert.assertEquals(true, instance.pconvert10(1.0));
			Assert.assertEquals(true, instance.pconvert11(1.0f));
			Assert.assertEquals(false, instance.pconvert12(0));
			Assert.assertEquals(false, instance.pconvert13(0L));
			Assert.assertEquals(false, instance.pconvert14((short)0));
			
			Assert.assertEquals(' ', instance.pconvert15((byte)32));
			Assert.assertEquals((char)1, instance.pconvert16(true));
			Assert.assertEquals(' ', instance.pconvert17(32.0));
			Assert.assertEquals(' ', instance.pconvert18(32.0f));
			Assert.assertEquals(' ', instance.pconvert19(32));
			Assert.assertEquals(' ', instance.pconvert20(32L));
			Assert.assertEquals(' ', instance.pconvert21((short)32));
			
			Assert.assertEquals(5.0, instance.pconvert22((byte)5));
			Assert.assertEquals(1.0, instance.pconvert23(true));
			Assert.assertEquals(32.0, instance.pconvert24(' '));
			Assert.assertEquals(10.0, instance.pconvert25(10.0f));
			Assert.assertEquals(10.0, instance.pconvert26(10));
			Assert.assertEquals(10.0, instance.pconvert27(10L));
			Assert.assertEquals(10.0, instance.pconvert28((short)10));
			
			Assert.assertEquals(5.0f, instance.pconvert29((byte)5));
			Assert.assertEquals(1.0f, instance.pconvert30(true));
			Assert.assertEquals(32.0f, instance.pconvert31(' '));
			Assert.assertEquals(10.0f, instance.pconvert32(10.0));
			Assert.assertEquals(10.0f, instance.pconvert33(10));
			Assert.assertEquals(10.0f, instance.pconvert34(10L));
			Assert.assertEquals(10.0f, instance.pconvert35((short)10));
			
			Assert.assertEquals(5, instance.pconvert36((byte)5));
			Assert.assertEquals(1, instance.pconvert37(true));
			Assert.assertEquals(32, instance.pconvert38(' '));
			Assert.assertEquals(10, instance.pconvert39(10.1));
			Assert.assertEquals(10, instance.pconvert40(10.3f));
			Assert.assertEquals(10, instance.pconvert41(10L));
			Assert.assertEquals(10, instance.pconvert42((short)10));
			
			Assert.assertEquals(5L, instance.pconvert43((byte)5));
			Assert.assertEquals(1L, instance.pconvert44(true));
			Assert.assertEquals(32L, instance.pconvert45(' '));
			Assert.assertEquals(10L, instance.pconvert46(10.1));
			Assert.assertEquals(10L, instance.pconvert47(10.3f));
			Assert.assertEquals(10L, instance.pconvert48(10));
			Assert.assertEquals(10L, instance.pconvert49((short)10));
			
			Assert.assertEquals((short)5, instance.pconvert50((byte)5));
			Assert.assertEquals((short)1, instance.pconvert51(true));
			Assert.assertEquals((short)32, instance.pconvert52(' '));
			Assert.assertEquals((short)10, instance.pconvert53(10.1));
			Assert.assertEquals((short)10, instance.pconvert54(10.3f));
			Assert.assertEquals((short)10, instance.pconvert55(10));
			Assert.assertEquals((short)10, instance.pconvert56(10L));
			
			
			
			
			
			
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
