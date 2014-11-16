package org.jasm.test.parser;

import org.junit.Test;

public class EnumerationAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/clazz/Enumeration.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
