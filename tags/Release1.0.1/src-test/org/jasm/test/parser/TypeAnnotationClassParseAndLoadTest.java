package org.jasm.test.parser;


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

}
