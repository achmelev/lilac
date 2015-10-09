package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.IntegerLiteral;
import org.junit.Test;

public class IntegerLiteralTest {
	
	@Test
	public void test() {
		IntegerLiteral l = new IntegerLiteral(0, 0, "gghj");
		Assert.assertFalse(l.isValid());
		
		l = new IntegerLiteral(0, 0, "1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "+1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "-1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "2147483649");
		Assert.assertFalse(l.isValid());
		
		l = new IntegerLiteral(0, 0, "-2147483649");
		Assert.assertFalse(l.isValid());
		
		l = new IntegerLiteral(0, 0, "0X4d6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "0x4D6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "-0X4d6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "02326");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		
		l = new IntegerLiteral(0, 0, "-02326");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "0B10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "0b10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new IntegerLiteral(0, 0, "-0B10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
	}

}
