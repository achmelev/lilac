package org.jasm.test.parser;



import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class SynteticDeprecatedParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "SynteticDeprecated.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		
		Assert.assertNotNull(clazz);
		
		
		
		
	
	}

}
