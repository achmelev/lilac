package org.jasm.test.parser;

import org.junit.Test;

public class Interface2WithExceptionsAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Interface2WithExceptions.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
