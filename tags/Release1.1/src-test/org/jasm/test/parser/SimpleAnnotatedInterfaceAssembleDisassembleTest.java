package org.jasm.test.parser;

import org.junit.Test;

public class SimpleAnnotatedInterfaceAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/SimpleAnnotatedInterface.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
