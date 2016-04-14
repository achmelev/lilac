package org.jasm.test.parser;

import org.junit.Test;

public class TypeAnnotationClassAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "org/jasm/test/testclass/TypeAnnotationClass.class";
	}
	
	@Test
	public void test() {
		doTest();
	}

}
