package org.jasm.test.parser;

import org.junit.Test;

public class Class2AssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Class2.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

	
	

}
