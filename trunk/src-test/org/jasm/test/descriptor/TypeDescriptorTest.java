package org.jasm.test.descriptor;

import junit.framework.Assert;

import org.jasm.type.descriptor.IllegalDescriptorException;
import org.jasm.type.descriptor.TypeDescriptor;
import org.junit.Test;

public class TypeDescriptorTest {
	
	@Test
	public void test() {
		
		TypeDescriptor desc = null;
		try {
			desc = new TypeDescriptor("B");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertTrue(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("C");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertTrue(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("D");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertTrue(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("F");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertTrue(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("I");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertTrue(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("J");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertTrue(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("S");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertTrue(desc.isShort());
		
		try {
			desc = new TypeDescriptor("Z");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertTrue(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("Ljava/lang/Object;");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(desc.isArray());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertTrue(desc.isObject());
		Assert.assertFalse(desc.isShort());
		Assert.assertEquals("java/lang/Object",desc.getClassName());
		
		try {
			desc = new TypeDescriptor("[Ljava/lang/Object;");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertTrue(desc.isArray());
		Assert.assertTrue(desc.getComponentType().isObject());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
		
		try {
			desc = new TypeDescriptor("[[I");
		} catch (IllegalDescriptorException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertTrue(desc.isArray());
		Assert.assertTrue(desc.getComponentType().isArray());
		Assert.assertTrue(desc.getComponentType().getComponentType().isInteger());
		Assert.assertFalse(desc.isBoolean());
		Assert.assertFalse(desc.isByte());
		Assert.assertFalse(desc.isCharacter());
		Assert.assertFalse(desc.isDouble());
		Assert.assertFalse(desc.isFloat());
		Assert.assertFalse(desc.isInteger());
		Assert.assertFalse(desc.isLong());
		Assert.assertFalse(desc.isObject());
		Assert.assertFalse(desc.isShort());
	}

}
