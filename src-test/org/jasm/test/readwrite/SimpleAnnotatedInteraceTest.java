package org.jasm.test.readwrite;

import org.jasm.item.attribute.Attributes;
import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleAnnotatedInteraceTest extends AbstractReadWriteTestCase {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();
		
	}
	
	private <T> T getAttributeContent(Attributes attrs, Class<T> clazz) {
		for (int i=0;i<attrs.getSize(); i++) {
			if (attrs.get(i).getContent().getClass().equals(clazz)) {
				return (T)attrs.get(i).getContent();
			}
		}
		return null;
		
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/SimpleAnnotatedInterface.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		// TODO Auto-generated method stub
		
	}

}
