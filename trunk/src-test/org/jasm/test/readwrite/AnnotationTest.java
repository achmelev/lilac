package org.jasm.test.readwrite;

import static org.junit.Assert.assertEquals;

import org.jasm.item.attribute.AnnotationDefaultAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationTest extends AbstractReadWriteTestCase { 
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();
		
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/TestAnnotation.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		assertEquals("dummy", ((AnnotationDefaultAttributeContent)clazz.getMethods().getMethod("stringValue", "()Ljava/lang/String;").getAttributes().get(0).getContent()).getValue().getPrimitiveValue());
		
	}

}
