package org.jasm.test.parser;


import org.jasm.item.clazz.Clazz;
import org.junit.Test;



public class TypeAnnotationClassParseAndLoadTest extends AbstractParseAndLoadTestCase {

	@Override
	protected String getDateiName() {
		return "TypeAnnotationClassT.jasm";
	}
	
	@Test
	public void test() {
		doTest();
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.testclass.TypeAnnotationClassT";
	}

	@Override
	protected void testClass(Class cl) {
		
		
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		// TODO Auto-generated method stub
		
	}

}
