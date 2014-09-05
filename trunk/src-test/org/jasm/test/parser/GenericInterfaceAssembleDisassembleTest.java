package org.jasm.test.parser;

import org.junit.Test;

public class GenericInterfaceAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/GenericInterface.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
