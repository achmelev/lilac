package org.jasm.test.identifier;

import org.jasm.item.utils.IdentifierUtils;
import org.junit.Test;
import junit.framework.Assert;

public class IdentifierUtilsTest {
	
	@Test
	public void test() {
		Assert.assertTrue(IdentifierUtils.isValidIdentifier("name"));
		Assert.assertTrue(IdentifierUtils.isValidIdentifier("n"));
		Assert.assertFalse(IdentifierUtils.isValidIdentifier("/na"));
		Assert.assertFalse(IdentifierUtils.isValidIdentifier(".na"));
		Assert.assertTrue(IdentifierUtils.isValidIdentifier("näme$2"));
		
		Assert.assertTrue(IdentifierUtils.isValidJasmClassName("name/name2"));
		Assert.assertFalse(IdentifierUtils.isValidJasmClassName("name//name2"));
		Assert.assertFalse(IdentifierUtils.isValidJasmClassName("name..name2"));
		
		Assert.assertTrue(IdentifierUtils.isValidJavaClassName("name.name2"));
		Assert.assertFalse(IdentifierUtils.isValidJavaClassName("name..name2"));
		Assert.assertFalse(IdentifierUtils.isValidJavaClassName(".name.name2"));
		
		Assert.assertEquals("name.name", IdentifierUtils.convertToJavaClassName("name/name"));
		Assert.assertEquals("name/name/n", IdentifierUtils.convertToJasmClassName("name.name.n"));
		
		Assert.assertTrue(IdentifierUtils.isValidIdentifier("java"));
		
		Assert.assertTrue(IdentifierUtils.isValidJasmClassName("java/lang/Object"));
	}

}
