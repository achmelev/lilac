package org.jasm.test.parser;

import org.jasm.item.clazz.Clazz;
import org.junit.Test;

public class Interface1ParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Interface1.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = parse();
	}

}
