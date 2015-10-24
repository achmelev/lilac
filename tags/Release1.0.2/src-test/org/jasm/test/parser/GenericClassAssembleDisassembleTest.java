package org.jasm.test.parser;

import org.junit.Test;

public class GenericClassAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/GenericClass.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
