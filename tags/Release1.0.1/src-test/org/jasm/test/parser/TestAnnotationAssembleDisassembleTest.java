package org.jasm.test.parser;

import org.junit.Test;

public class TestAnnotationAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/TestAnnotation.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
