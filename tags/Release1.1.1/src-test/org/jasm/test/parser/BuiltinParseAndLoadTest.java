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
			
			//Concat
			IBuiltinMacros instance = (IBuiltinMacros)cl.newInstance();
			String result = instance.concat("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertEquals("Anfang MyString: 1, true, X, 1.0, 1.5, 10, 100, 25", result);
			result = instance.concat2("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertEquals("MyString MyString: 1, true, X, 1.0, 1.5, 10, 100, 25", result);
			result = instance.concat4("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertEquals("MyString MyString: 1, true, X, 1.0, 1.5, 10, 100, 25", result);
			result = instance.concat5("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertEquals("MyString MyString: 1, true, X, 1.0, 1.5, 10, 100, 25", result);
			
			//Print
			instance.println("Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			PrintStream pstream = new PrintStream(stream);
			instance.sprintln(pstream, "Anfang", (byte)1, true, 'X', 1.0, 1.5f, 10, (long)100, (short)25);
			Assert.assertTrue(new String(stream.toByteArray()).startsWith("Anfang MyString: 1, true, X, 1.0, 1.5, 10, 100, 25"));
			
			//Primitive casts
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
			
			//Object to Primitive casts
			Assert.assertEquals(1, instance.opconvert1(true));
			Assert.assertEquals(1, instance.opconvert2((char)1));
			Assert.assertEquals(1, instance.opconvert3(1.0));
			Assert.assertEquals(1, instance.opconvert4(1.0f));
			Assert.assertEquals(1, instance.opconvert5(1));
			Assert.assertEquals(1, instance.opconvert6(1L));
			Assert.assertEquals(1, instance.opconvert7((short)1));
			
			Assert.assertEquals(false, instance.opconvert8((byte)0));
			Assert.assertEquals(true, instance.opconvert9((' ')));
			Assert.assertEquals(true, instance.opconvert10(1.0));
			Assert.assertEquals(true, instance.opconvert11(1.0f));
			Assert.assertEquals(false, instance.opconvert12(0));
			Assert.assertEquals(false, instance.opconvert13(0L));
			Assert.assertEquals(false, instance.opconvert14((short)0));
			
			Assert.assertEquals(' ', instance.opconvert15((byte)32));
			Assert.assertEquals((char)1, instance.opconvert16(true));
			Assert.assertEquals(' ', instance.opconvert17(32.0));
			Assert.assertEquals(' ', instance.opconvert18(32.0f));
			Assert.assertEquals(' ', instance.opconvert19(32));
			Assert.assertEquals(' ', instance.opconvert20(32L));
			Assert.assertEquals(' ', instance.opconvert21((short)32));
			
			Assert.assertEquals(5.0, instance.opconvert22((byte)5));
			Assert.assertEquals(1.0, instance.opconvert23(true));
			Assert.assertEquals(32.0, instance.opconvert24(' '));
			Assert.assertEquals(10.0, instance.opconvert25(10.0f));
			Assert.assertEquals(10.0, instance.opconvert26(10));
			Assert.assertEquals(10.0, instance.opconvert27(10L));
			Assert.assertEquals(10.0, instance.opconvert28((short)10));
			
			Assert.assertEquals(5.0f, instance.opconvert29((byte)5));
			Assert.assertEquals(1.0f, instance.opconvert30(true));
			Assert.assertEquals(32.0f, instance.opconvert31(' '));
			Assert.assertEquals(10.0f, instance.opconvert32(10.0));
			Assert.assertEquals(10.0f, instance.opconvert33(10));
			Assert.assertEquals(10.0f, instance.opconvert34(10L));
			Assert.assertEquals(10.0f, instance.opconvert35((short)10));
			
			Assert.assertEquals(5, instance.opconvert36((byte)5));
			Assert.assertEquals(1, instance.opconvert37(true));
			Assert.assertEquals(32, instance.opconvert38(' '));
			Assert.assertEquals(10, instance.opconvert39(10.1));
			Assert.assertEquals(10, instance.opconvert40(10.3f));
			Assert.assertEquals(10, instance.opconvert41(10L));
			Assert.assertEquals(10, instance.opconvert42((short)10));
			
			Assert.assertEquals(5L, instance.opconvert43((byte)5));
			Assert.assertEquals(1L, instance.opconvert44(true));
			Assert.assertEquals(32L, instance.opconvert45(' '));
			Assert.assertEquals(10L, instance.opconvert46(10.1));
			Assert.assertEquals(10L, instance.opconvert47(10.3f));
			Assert.assertEquals(10L, instance.opconvert48(10));
			Assert.assertEquals(10L, instance.opconvert49((short)10));
			
			Assert.assertEquals((short)5, instance.opconvert50((byte)5));
			Assert.assertEquals((short)1, instance.opconvert51(true));
			Assert.assertEquals((short)32, instance.opconvert52(' '));
			Assert.assertEquals((short)10, instance.opconvert53(10.1));
			Assert.assertEquals((short)10, instance.opconvert54(10.3f));
			Assert.assertEquals((short)10, instance.opconvert55(10));
			Assert.assertEquals((short)10, instance.opconvert56(10L));
			
			Assert.assertEquals(10, instance.unbox((byte)10));
			Assert.assertEquals(true, instance.unbox(Boolean.TRUE));
			Assert.assertEquals('X', instance.unbox('X'));
			Assert.assertEquals(-1.0, instance.unbox(-1.0));
			Assert.assertEquals(-1.05f, instance.unbox(-1.05f));
			Assert.assertEquals(55, instance.unbox(55));
			Assert.assertEquals(-100000L, instance.unbox(-100000L));
			Assert.assertEquals(-33, instance.unbox((short)-33));
			
			Assert.assertEquals((byte)1, instance.nunbox1(1));
			Assert.assertEquals(false, instance.nunbox2(0));
			Assert.assertEquals(' ', instance.nunbox3(32));
			Assert.assertEquals(-5.0, instance.nunbox4(-5));
			Assert.assertEquals(-5.5f, instance.nunbox5(-5.5));
			Assert.assertEquals(10, instance.nunbox6(10));
			Assert.assertEquals(10000L, instance.nunbox7(10000));
			Assert.assertEquals((short)123, instance.nunbox8(123));
			
			//Primitive to object casts
			Assert.assertEquals(new Byte((byte)1), instance.poconvert1(true));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert2((char)1));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert3(1.0));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert4(1.0f));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert5(1));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert6(1L));
			Assert.assertEquals(new Byte((byte)1), instance.poconvert7((short)1));
			
			Assert.assertEquals(Boolean.FALSE, instance.poconvert8((byte)0));
			Assert.assertEquals(Boolean.TRUE, instance.poconvert9((' ')));
			Assert.assertEquals(Boolean.TRUE, instance.poconvert10(1.0));
			Assert.assertEquals(Boolean.TRUE, instance.poconvert11(1.0f));
			Assert.assertEquals(Boolean.FALSE, instance.poconvert12(0));
			Assert.assertEquals(Boolean.FALSE, instance.poconvert13(0L));
			Assert.assertEquals(Boolean.FALSE, instance.poconvert14((short)0));
			
			Assert.assertEquals(new Character(' '), instance.poconvert15((byte)32));
			Assert.assertEquals(new Character((char)1), instance.poconvert16(true));
			Assert.assertEquals(new Character(' '), instance.poconvert17(32.0));
			Assert.assertEquals(new Character(' '), instance.poconvert18(32.0f));
			Assert.assertEquals(new Character(' '), instance.poconvert19(32));
			Assert.assertEquals(new Character(' '), instance.poconvert20(32L));
			Assert.assertEquals(new Character(' '), instance.poconvert21((short)32));
			
			Assert.assertEquals(5.0, instance.poconvert22((byte)5));
			Assert.assertEquals(1.0, instance.poconvert23(true));
			Assert.assertEquals(32.0, instance.poconvert24(' '));
			Assert.assertEquals(10.0, instance.poconvert25(10.0f));
			Assert.assertEquals(10.0, instance.poconvert26(10));
			Assert.assertEquals(10.0, instance.poconvert27(10L));
			Assert.assertEquals(10.0, instance.poconvert28((short)10));
			
			Assert.assertEquals(5.0f, instance.poconvert29((byte)5));
			Assert.assertEquals(1.0f, instance.poconvert30(true));
			Assert.assertEquals(32.0f, instance.poconvert31(' '));
			Assert.assertEquals(10.0f, instance.poconvert32(10.0));
			Assert.assertEquals(10.0f, instance.poconvert33(10));
			Assert.assertEquals(10.0f, instance.poconvert34(10L));
			Assert.assertEquals(10.0f, instance.poconvert35((short)10));
			
			Assert.assertEquals(new Integer(5), instance.poconvert36((byte)5));
			Assert.assertEquals(new Integer(1), instance.poconvert37(true));
			Assert.assertEquals(new Integer(32), instance.poconvert38(' '));
			Assert.assertEquals(new Integer(10), instance.poconvert39(10.1));
			Assert.assertEquals(new Integer(10), instance.poconvert40(10.3f));
			Assert.assertEquals(new Integer(10), instance.poconvert41(10L));
			Assert.assertEquals(new Integer(10), instance.poconvert42((short)10));
			
			Assert.assertEquals(new Long(5L), instance.poconvert43((byte)5));
			Assert.assertEquals(new Long(1L), instance.poconvert44(true));
			Assert.assertEquals(new Long(32L), instance.poconvert45(' '));
			Assert.assertEquals(new Long(10L), instance.poconvert46(10.1));
			Assert.assertEquals(new Long(10L), instance.poconvert47(10.3f));
			Assert.assertEquals(new Long(10L), instance.poconvert48(10));
			Assert.assertEquals(new Long(10L), instance.poconvert49((short)10));
			
			Assert.assertEquals(new Short((short)5), instance.poconvert50((byte)5));
			Assert.assertEquals(new Short((short)1), instance.poconvert51(true));
			Assert.assertEquals(new Short((short)32), instance.poconvert52(' '));
			Assert.assertEquals(new Short((short)10), instance.poconvert53(10.1));
			Assert.assertEquals(new Short((short)10), instance.poconvert54(10.3f));
			Assert.assertEquals(new Short((short)10), instance.poconvert55(10));
			Assert.assertEquals(new Short((short)10), instance.poconvert56(10L));
			
			Assert.assertEquals(new Byte((byte)1), instance.box((byte)1));
			Assert.assertEquals(Boolean.TRUE, instance.box(true));
			Assert.assertEquals(new Character('Y'), instance.box('Y'));
			Assert.assertEquals(new Double(1.1), instance.box(1.1));
			Assert.assertEquals(new Float(1.1f), instance.box(1.1f));
			Assert.assertEquals(new Integer(10), instance.box(10));
			Assert.assertEquals(new Long(10000L), instance.box(10000L));
			Assert.assertEquals(new Short((short)100), instance.box((short)100));
			
			Assert.assertEquals(new Byte((byte)1), instance.nbox((byte)1));
			Assert.assertEquals(new Integer(1), instance.nbox(true));
			Assert.assertEquals(new Integer(32), instance.nbox(' '));
			Assert.assertEquals(new Double(1.1), instance.nbox(1.1));
			Assert.assertEquals(new Float(1.1f), instance.nbox(1.1f));
			Assert.assertEquals(new Integer(10), instance.nbox(10));
			Assert.assertEquals(new Long(10000L), instance.nbox(10000L));
			Assert.assertEquals(new Short((short)100), instance.nbox((short)100));
			
			int [] [] intArray = new int [10] [6];
			instance.setIntArray(intArray);
			Assert.assertSame(instance.getIntArray(), intArray);
			
			MyRunnable r = new MyRunnable();
			instance.setObject(r);
			Assert.assertSame(instance.getObject(), r);
			
			MyRunnable [] ar = new MyRunnable[10];
			instance.setObjectArray(ar);
			Assert.assertSame(instance.getObjectArray(), ar);
			
			TestBean bean = instance.createTestBean(5, false, "Hello", r);
			Assert.assertEquals((short)5, bean.getIntValue());
			Assert.assertEquals(false, bean.isBooleanValue());
			Assert.assertEquals("Hello", bean.getStringValue());
			Assert.assertSame(r, bean.getRunnable());
			
			boolean [] boolArray = instance.createBooleanArray(new Double(10.0));
			Assert.assertEquals(10, boolArray.length);
			
			Runnable [] runnableArray = instance.createRunnableArray(20L);
			Assert.assertEquals(20, runnableArray.length);
			
			boolean [] [] boolArray2 = instance.createBooleanArray2(new Double(10), 20L);
			Assert.assertEquals(10, boolArray2.length);
			Assert.assertEquals(20, boolArray2[0].length);
					
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
