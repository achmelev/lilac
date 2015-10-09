package org.jasm.test.readwrite;

import org.jasm.item.attribute.EnclosingMethodAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerClassTest1 extends AbstractReadWriteTestCase {
	
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
		Assert.assertEquals("org/jasm/test/testclass/Class2", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getClassName());
		Assert.assertEquals("methodWithAnonymousClass", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getMethodName());
		Assert.assertEquals("()V", ((EnclosingMethodAttributeContent)clazz.getAttributes().get(1).getContent()).getMethodDescriptor());
		
	}

}
