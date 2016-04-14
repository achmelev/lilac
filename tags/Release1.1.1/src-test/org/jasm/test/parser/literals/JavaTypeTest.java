package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.junit.Test;

public class JavaTypeTest {
	
	@Test
	public void test() {
		JavaTypeLiteral jt = new JavaTypeLiteral(0, 0, "boolean");
		Assert.assertTrue(jt.getDescriptor().isBoolean());
		
		jt = new JavaTypeLiteral(0, 0, "boolean []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isBoolean());
		
		jt = new JavaTypeLiteral(0, 0, "boolean [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isBoolean());
		
		jt = new JavaTypeLiteral(0, 0, "byte");
		Assert.assertTrue(jt.getDescriptor().isByte());
		
		jt = new JavaTypeLiteral(0, 0, "byte []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isByte());
		
		jt = new JavaTypeLiteral(0, 0, "byte [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isByte());
		
		jt = new JavaTypeLiteral(0, 0, "char");
		Assert.assertTrue(jt.getDescriptor().isCharacter());
		
		jt = new JavaTypeLiteral(0, 0, "char []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isCharacter());
		
		jt = new JavaTypeLiteral(0, 0, "char [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isCharacter());
		
		jt = new JavaTypeLiteral(0, 0, "double");
		Assert.assertTrue(jt.getDescriptor().isDouble());
		
		jt = new JavaTypeLiteral(0, 0, "double []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isDouble());
		
		jt = new JavaTypeLiteral(0, 0, "double [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isDouble());
		
		jt = new JavaTypeLiteral(0, 0, "float");
		Assert.assertTrue(jt.getDescriptor().isFloat());
		
		jt = new JavaTypeLiteral(0, 0, "float []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isFloat());
		
		jt = new JavaTypeLiteral(0, 0, "float [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isFloat());
		
		jt = new JavaTypeLiteral(0, 0, "int");
		Assert.assertTrue(jt.getDescriptor().isInteger());
		
		jt = new JavaTypeLiteral(0, 0, "int []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isInteger());
		
		jt = new JavaTypeLiteral(0, 0, "int [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isInteger());
		
		jt = new JavaTypeLiteral(0, 0, "long");
		Assert.assertTrue(jt.getDescriptor().isLong());
		
		jt = new JavaTypeLiteral(0, 0, "long []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isLong());
		
		jt = new JavaTypeLiteral(0, 0, "long [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isLong());
		
		jt = new JavaTypeLiteral(0, 0, "short");
		Assert.assertTrue(jt.getDescriptor().isShort());
		
		jt = new JavaTypeLiteral(0, 0, "short []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().isShort());
		
		jt = new JavaTypeLiteral(0, 0, "short [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().isShort());
		
		jt = new JavaTypeLiteral(0, 0, "/java/lang/String");
		Assert.assertTrue(jt.getDescriptor().isObject());
		Assert.assertTrue(jt.getDescriptor().getClassName().equals("java/lang/String"));
		
		jt = new JavaTypeLiteral(0, 0, "java/lang/String []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 1);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getClassName().equals("java/lang/String"));
		
		jt = new JavaTypeLiteral(0, 0, "java/lang/String [] []");
		Assert.assertTrue(jt.getDescriptor().isArray());
		Assert.assertEquals(jt.getDescriptor().getArrayDimension(), 2);
		Assert.assertTrue(jt.getDescriptor().getComponentType().getComponentType().getClassName().equals("java/lang/String"));
		
		
		
	}
	

}
