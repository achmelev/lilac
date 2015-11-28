package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.FieldReference;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.MethodReference;
import org.junit.Test;

public class MethodReferenceTest {
	
	@Test
	public void test() {
		MethodReference ref = new MethodReference(0, 0, "org/jasm/test/Test/voidMethod@()V");
		Assert.assertEquals(true, ref.isValid());
   		Assert.assertEquals("org/jasm/test/Test", ref.getClassName());
		Assert.assertEquals("voidMethod", ref.getMethodName());
		Assert.assertEquals(null, ref.getDescriptor().getReturnType());
		Assert.assertEquals(0, ref.getDescriptor().getParameters().size());
		
		ref = new MethodReference(0, 0, "org/jasm/test/Test/booleanMethod@(I)Z");
		Assert.assertEquals(true, ref.isValid());
   		Assert.assertEquals("org/jasm/test/Test", ref.getClassName());
		Assert.assertEquals("booleanMethod", ref.getMethodName());
		Assert.assertEquals(true, ref.getDescriptor().getReturnType().isBoolean());
		Assert.assertEquals(1, ref.getDescriptor().getParameters().size());
		Assert.assertEquals(true, ref.getDescriptor().getParameters().get(0).isInteger());
		
		ref = new MethodReference(0, 0, "booleanMethod@(I)Z");
		Assert.assertEquals("", ref.getClassName());
		Assert.assertEquals("booleanMethod", ref.getMethodName());
		Assert.assertEquals(true, ref.getDescriptor().getReturnType().isBoolean());
		Assert.assertEquals(1, ref.getDescriptor().getParameters().size());
		Assert.assertEquals(true, ref.getDescriptor().getParameters().get(0).isInteger());
		
	}
	

}
