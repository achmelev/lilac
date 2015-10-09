package org.jasm.test.parser;

import org.junit.Test;

public class AnnotatedInterfaceAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/AnnotatedInterface.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
