package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.LongLiteral;
import org.junit.Test;

public class LongLiteralTest {
	
	@Test
	public void test() {
		LongLiteral l = new LongLiteral(0, 0, "gghj");
		Assert.assertFalse(l.isValid());
		
		l = new LongLiteral(0, 0, "1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "+1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "-1238");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new LongLiteral(0, 0, "2147483649");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(2147483649l, l.getValue());
		
		l = new LongLiteral(0, 0, "-2147483649");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-2147483649l, l.getValue());
		
		l = new LongLiteral(0, 0, "0X4d6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "0x4D6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "-0X4d6");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new LongLiteral(0, 0, "02326");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		
		l = new LongLiteral(0, 0, "-02326");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
		l = new LongLiteral(0, 0, "0B10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "0b10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(1238, l.getValue());
		
		l = new LongLiteral(0, 0, "-0B10011010110");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals(-1238, l.getValue());
		
	}

}
