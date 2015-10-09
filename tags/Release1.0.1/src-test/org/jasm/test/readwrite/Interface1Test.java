package org.jasm.test.readwrite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interface1Test extends AbstractReadWriteTestCase {
	
    private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		doTest();
		
	}

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Interface1.class";
	}

	@Override
	protected void doChecks(Clazz clazz) {
		assertEquals(clazz.getThisClass().getClassName(),"org/jasm/test/testclass/Interface1");
		assertEquals(clazz.getSuperClass().getClassName(),"java/lang/Object");
		assertEquals(clazz.getInterfaces().size(), 0);
		assertEquals(clazz.getAttributes().getSize(), 1);
		assertTrue(clazz.getModifier().isPublic());
		assertTrue(clazz.getModifier().isAbstract());
		assertFalse(clazz.getModifier().isAnnotation());
		assertFalse(clazz.getModifier().isEnum());
		assertFalse(clazz.getModifier().isFinal());
		assertTrue(clazz.getModifier().isInterface());
		assertFalse(clazz.getModifier().isSuper());
		
	}

}
