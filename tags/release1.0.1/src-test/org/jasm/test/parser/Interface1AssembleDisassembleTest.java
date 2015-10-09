package org.jasm.test.parser;

import org.junit.Test;

public class Interface1AssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Interface1.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
