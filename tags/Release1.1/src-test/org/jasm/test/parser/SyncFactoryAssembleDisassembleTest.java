package org.jasm.test.parser;

import org.junit.Test;

public class SyncFactoryAssembleDisassembleTest extends AbstractDisassembleAssembleTestCase {

	@Override
	protected String getClassResourceName() {
		return "javax/sql/rowset/spi/SyncFactory.class";
	}
	
	@Test
	public void test() {
		doTest();
	}
	
	

}
