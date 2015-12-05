package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class MethodHandleParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "MethodHandleExample.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.MethodHandleExampleT";
	}

	@Override
	protected void testClass(Class cl) {
		try {
			IMethodHandle2 obj = (IMethodHandle2)cl.newInstance();
			obj.initHandles();
			obj.callPutA(1);
			Assert.assertEquals(1, obj.callGetA());
			obj.callSetA1(2);
			Assert.assertEquals(2, obj.callGetA());
			obj.callSetA2(3);
			Assert.assertEquals(3, obj.callGetA());
			obj.callSetA3(4);
			Assert.assertEquals(4, obj.callGetA());
			obj.callSetB(5);
			Assert.assertEquals(5, obj.callGetB());
			obj.callPutB(6);
			Assert.assertEquals(6, obj.callGetB());
			Assert.assertEquals(10, obj.callConstructor(10));
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
		// TODO Auto-generated method stub
		
	}
	
	

}
