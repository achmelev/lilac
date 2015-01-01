package org.jasm.test.parser;



import org.jasm.item.attribute.InnerClass;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;



public class ThreadPoolWorkerParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "ThreadPoolWorker.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = parse();
		if (parser.getErrorMessages().size() > 0) {
			parser.printErrors();
			Assert.fail("Parsing failed!");
		} 
		
		Assert.assertNotNull(clazz);
		
		
	
	}

}
