package org.jasm.test.disassembler;

import junit.framework.Assert;

import org.jasm.disassembler.NameGenerator;
import org.junit.Test;

public class NameGeneratorTest {
	
	@Test
	public void test() {
		NameGenerator gen = new NameGenerator();
		
		Assert.assertEquals("setA", gen.generateName("setA"));
		Assert.assertEquals("setA$0", gen.generateName("setA"));
		Assert.assertEquals("public$0", gen.generateName("public"));
		Assert.assertEquals("public$1", gen.generateName("public"));
		Assert.assertEquals("public$2", gen.generateName("public"));
	}

}
