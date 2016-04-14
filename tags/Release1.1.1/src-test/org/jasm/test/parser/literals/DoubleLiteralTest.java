package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.IntegerLiteral;
import org.junit.Test;

public class DoubleLiteralTest {
	
	@Test
	public void test() {
		DoubleLiteral l = new DoubleLiteral(0, 0, "gghj");
		Assert.assertFalse(l.isValid());
		
		l = new DoubleLiteral(0, 0, "1238.5");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals("1238.5", ""+l.getValue());
		

		
		double f = 0.5;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = 32.5;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = -32.5;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = -0.5;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.01p0;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.fffffffffffffp0;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = -0x1.fffffffffffffp0;
		Assert.assertEquals(f, new DoubleLiteral(0, 0, DoubleLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.9066666666666p6;
		Assert.assertEquals("1.9066666666666p6", DoubleLiteral.createExactHexLiteral(f));
		f = -0x1.9066666666666p6;
		Assert.assertEquals("-1.9066666666666p6", DoubleLiteral.createExactHexLiteral(f));
		
	}
	

}
