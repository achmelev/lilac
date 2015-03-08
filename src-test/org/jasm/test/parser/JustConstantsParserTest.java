package org.jasm.test.parser;


import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class JustConstantsParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "JustConstants.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		
		Assert.assertNotNull(clazz);
		
		
		Assert.assertEquals(51, clazz.getMajorVersion());
		Assert.assertEquals(0, clazz.getMinorVersion());
		Assert.assertTrue(clazz.getModifier().isAbstract());
		Assert.assertFalse(clazz.getModifier().isAnnotation());
		Assert.assertFalse(clazz.getModifier().isEnum());
		Assert.assertFalse(clazz.getModifier().isFinal());
		Assert.assertFalse(clazz.getModifier().isInterface());
		Assert.assertTrue(clazz.getModifier().isPublic());
		Assert.assertFalse(clazz.getModifier().isSuper());
		Assert.assertFalse(clazz.getModifier().isSyntetic());
		
		Assert.assertEquals("org/jasm/test/testclass/JustConstants", clazz.getThisClass().getClassName());
		Assert.assertEquals("java/lang/Object", clazz.getSuperClass().getClassName());
		
		Assert.assertEquals(27, clazz.getConstantPool().getSize());
		Assert.assertEquals(0, clazz.getAttributes().getSize());
		
		Assert.assertEquals(1,clazz.getConstantPool().getInterfaceMethodRef("org/jasm/test/testclass/Interface1", "testMethod", "(I)V").size());

		
	
	}

}
