package org.jasm.test.parser;

import org.junit.Test;

public class DaysAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/Days.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
