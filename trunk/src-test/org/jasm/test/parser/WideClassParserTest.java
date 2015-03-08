package org.jasm.test.parser;


import java.util.List;


import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class WideClassParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "WideClass.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		
		Assert.assertNotNull(clazz);
		

		
	
	}

}
