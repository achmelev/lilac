package org.jasm.test.parser;



import org.jasm.item.attribute.InnerClass;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;



public class Class2ParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Class2.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
	
		
		Assert.assertNotNull(clazz);
		
		InnerClassesAttributeContent content = (InnerClassesAttributeContent)clazz.getAttributes().get(1).getContent();
		
		Assert.assertEquals(2,content.getSize());
		
		InnerClass innerClass1 = content.get(0);
		Assert.assertEquals(innerClass1.getInnerClassName(), "org/jasm/test/testclass/Class2$1");
		Assert.assertNull(innerClass1.getOuterClassName());
		Assert.assertNull(innerClass1.getInnerNameValue());
		Assert.assertTrue(innerClass1.getModifier().hasNoFlags());
		InnerClass innerClass2 = content.get(1);
		Assert.assertEquals(innerClass2.getInnerClassName(), "org/jasm/test/testclass/Class2$InnerClass");
		Assert.assertEquals(innerClass2.getOuterClassName(), "org/jasm/test/testclass/Class2");
		Assert.assertEquals(innerClass2.getInnerNameValue(), "InnerClass");
		Assert.assertTrue(innerClass2.getModifier().isPrivate());
		
		
	
	}

}
