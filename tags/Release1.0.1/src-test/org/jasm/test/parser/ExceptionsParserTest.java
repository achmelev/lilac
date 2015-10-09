package org.jasm.test.parser;


import java.util.List;


import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class ExceptionsParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Exceptions.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		 
		
		Assert.assertNotNull(clazz);
		
		List<Attribute> exceptions = clazz.getMethods().get(0).getAttributes().getAttributesByContentType(ExceptionsAttributeContent.class);
		
		Assert.assertEquals(1, exceptions.size());
		ExceptionsAttributeContent content = (ExceptionsAttributeContent)exceptions.get(0).getContent();
		Assert.assertEquals(2, content.getExceptionClassNames().size());
		Assert.assertTrue(content.getExceptionClassNames().contains("java/io/IOException"));
		Assert.assertTrue(content.getExceptionClassNames().contains("java/lang/IllegalArgumentException"));
		
	
	}

}
