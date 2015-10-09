package org.jasm.test.parser;

import org.junit.Test;

public class WideClassAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/WideClass.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
