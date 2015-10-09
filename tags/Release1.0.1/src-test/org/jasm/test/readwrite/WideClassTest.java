package org.jasm.test.readwrite;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WideClassTest extends AbstractReadWriteTestCase{
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();	
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/WideClass.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		
	}

}
