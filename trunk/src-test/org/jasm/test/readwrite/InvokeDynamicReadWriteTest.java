package org.jasm.test.readwrite;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;

public class InvokeDynamicReadWriteTest extends AbstractReadWriteTestCase {
	
	@Test
	public void test() {
		doTest();
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/InvokeDynamicClass.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		// TODO Auto-generated method stub
		
	}

}
