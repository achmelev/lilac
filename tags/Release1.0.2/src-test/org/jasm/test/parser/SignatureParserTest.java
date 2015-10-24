package org.jasm.test.parser;


import java.util.List;


import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.SignatureAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class SignatureParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Signature.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		
		Assert.assertNotNull(clazz);
		
		List<Attribute> signatures = clazz.getAttributes().getAttributesByContentType(SignatureAttributeContent.class);
		
		Assert.assertEquals(1, signatures.size());
		SignatureAttributeContent content = (SignatureAttributeContent)signatures.get(0).getContent();
		Assert.assertEquals("<T:Ljava/lang/Object;>Ljava/lang/Object;", content.getValue());
		
		signatures = clazz.getFields().get(0).getAttributes().getAttributesByContentType(SignatureAttributeContent.class);
		
		Assert.assertEquals(1, signatures.size());
		content = (SignatureAttributeContent)signatures.get(0).getContent();
		Assert.assertEquals("TT;", content.getValue());
		
		signatures = clazz.getMethods().get(0).getAttributes().getAttributesByContentType(SignatureAttributeContent.class);
		
		Assert.assertEquals(1, signatures.size());
		content = (SignatureAttributeContent)signatures.get(0).getContent();
		Assert.assertEquals("()TT;", content.getValue());
		
		
	
	}

}
