package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.FieldReference;
import org.jasm.parser.literals.IntegerLiteral;
import org.junit.Test;

public class FieldReferenceTest {
	
	@Test
	public void test() {
		FieldReference ref = new FieldReference(0, 0, "org/jasm/test/Test/intField@I");
		Assert.assertEquals(true, ref.isValid());
   		Assert.assertEquals("org/jasm/test/Test", ref.getClassName());
		Assert.assertEquals("intField", ref.getFieldName());
		Assert.assertEquals(true, ref.getDescriptor().isInteger());
		
		ref = new FieldReference(0, 0, "org/jasm/test/Test/arrayField@[I");
		Assert.assertEquals(true, ref.isValid());
   		Assert.assertEquals("org/jasm/test/Test", ref.getClassName());
		Assert.assertEquals("arrayField", ref.getFieldName());
		Assert.assertEquals(true, ref.getDescriptor().isArray());
		Assert.assertEquals(1, ref.getDescriptor().getArrayDimension());
		Assert.assertEquals(true, ref.getDescriptor().getComponentType().isInteger());
		
		ref = new FieldReference(0, 0, "arrayField@[I");
		Assert.assertEquals("", ref.getClassName());
		Assert.assertEquals("arrayField", ref.getFieldName());
		Assert.assertEquals(true, ref.getDescriptor().isArray());
		Assert.assertEquals(1, ref.getDescriptor().getArrayDimension());
		Assert.assertEquals(true, ref.getDescriptor().getComponentType().isInteger());
		
		
	}
	

}
