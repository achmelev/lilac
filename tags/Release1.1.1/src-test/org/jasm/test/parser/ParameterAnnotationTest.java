package org.jasm.test.parser;

import org.junit.Test;

public class ParameterAnnotationTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/ParameterAnnotatedClass.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
