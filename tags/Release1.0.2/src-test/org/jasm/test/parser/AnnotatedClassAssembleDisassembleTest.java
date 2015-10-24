package org.jasm.test.parser;

import org.junit.Test;

public class AnnotatedClassAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/AnnotatedClass.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
