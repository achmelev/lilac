package org.jasm.test.parser;

import org.junit.Test;

public class ObjectAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "java/lang/Object.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
