package org.jasm.test.readwrite;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Class21Test extends AbstractReadWriteTest {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();
		
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Class2$1.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		
		
	}
	
	

}
