package org.jasm.test.parser;

import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class Interface1ParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Interface1.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = parse();
		Assert.assertNotNull(clazz);
		
		
		/*Assert.assertEquals(51, clazz.getMajorVersion());
		Assert.assertEquals(0, clazz.getMinorVersion());
		Assert.assertTrue(clazz.getModifier().isAbstract());
		Assert.assertFalse(clazz.getModifier().isAnnotation());
		Assert.assertFalse(clazz.getModifier().isEnum());
		Assert.assertFalse(clazz.getModifier().isFinal());
		Assert.assertTrue(clazz.getModifier().isInterface());
		Assert.assertTrue(clazz.getModifier().isPublic());
		Assert.assertFalse(clazz.getModifier().isSuper());
		Assert.assertFalse(clazz.getModifier().isSyntetic());*/
		
		Assert.assertEquals(8, clazz.getConstantPool().getSize());
		Assert.assertEquals(1, clazz.getAttributes().getSize());
	}

}
