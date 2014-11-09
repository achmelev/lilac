package org.jasm.test.parser;

import org.junit.Test;

public class Class1AssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Class1.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
