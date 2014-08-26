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
		Assert.assertEquals(51, clazz.getMajorVersion());
		Assert.assertEquals(0, clazz.getMinorVersion());
	}

}
