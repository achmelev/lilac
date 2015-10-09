package org.jasm.test.parser;

import org.junit.Test;

public class Class3AssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Class3.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
