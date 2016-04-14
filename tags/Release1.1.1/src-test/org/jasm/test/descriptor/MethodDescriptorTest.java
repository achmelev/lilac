package org.jasm.test.descriptor;



import junit.framework.Assert;

import org.jasm.item.clazz.Method;
import org.jasm.type.descriptor.MethodDescriptor;
import org.junit.Test;

public class MethodDescriptorTest {
	
	@Test
	public void test() {
		String s = "()V";
		MethodDescriptor desc = new MethodDescriptor(s);
		
		Assert.assertNull(desc.getReturnType());
		Assert.assertEquals(0, desc.getParameters().size());
		testConstructor(desc);
		
		s = "()I";
		desc = new MethodDescriptor(s);
		Assert.assertTrue(desc.getReturnType().isInteger());
		Assert.assertEquals(0, desc.getParameters().size());
		testConstructor(desc);
		
		s = "()Ljava/lang/Object;";
		desc = new MethodDescriptor(s);
		Assert.assertTrue(desc.getReturnType().isObject());
		Assert.assertEquals("java/lang/Object",desc.getReturnType().getClassName());
		Assert.assertEquals(0, desc.getParameters().size());
		testConstructor(desc);
		
		s = "(Ljava/lang/String;IZ[J)Ljava/lang/Object;";
		desc = new MethodDescriptor(s);
		Assert.assertTrue(desc.getReturnType().isObject());
		Assert.assertEquals("java/lang/Object",desc.getReturnType().getClassName());
		Assert.assertEquals(4, desc.getParameters().size());
		Assert.assertTrue(desc.getParameters().get(0).isObject());
		Assert.assertEquals("java/lang/String",desc.getParameters().get(0).getClassName());
		Assert.assertTrue(desc.getParameters().get(1).isInteger());
		Assert.assertTrue(desc.getParameters().get(2).isBoolean());
		Assert.assertTrue(desc.getParameters().get(3).isArray());
		Assert.assertTrue(desc.getParameters().get(3).getComponentType().isLong());
		testConstructor(desc);
	}
	
	private void testConstructor(MethodDescriptor desc) {
		MethodDescriptor desc1 = new MethodDescriptor(desc.getParameters(), desc.getReturnType());
		Assert.assertEquals(desc.getValue(), desc1.getValue());
	}

}
