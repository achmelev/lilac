package org.jasm.test.parser;


import java.util.ArrayList;

import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ClassInfo;
import org.junit.Assert;
import org.junit.Test;

public class ImplementsParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "Implements.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		Assert.assertNotNull(clazz);
		
		
		Assert.assertEquals(2,clazz.getInterfaces().size());
		ArrayList<String> names = new ArrayList<>();
		for (ClassInfo intf: clazz.getInterfaces()) {
			names.add(intf.getClassName());
		}
		Assert.assertTrue(names.contains("org/jasm/test/testclass/Interface1"));
		Assert.assertTrue(names.contains("org/jasm/test/testclass/Interface2"));

		
	
	}

}
