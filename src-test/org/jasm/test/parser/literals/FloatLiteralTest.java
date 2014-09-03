package org.jasm.test.parser.literals;

import junit.framework.Assert;

import org.jasm.parser.literals.FloatLiteral;
import org.jasm.parser.literals.IntegerLiteral;
import org.junit.Test;

public class FloatLiteralTest {
	
	@Test
	public void test() {
		FloatLiteral l = new FloatLiteral(0, 0, "gghj");
		Assert.assertFalse(l.isValid());
		
		l = new FloatLiteral(0, 0, "1238.5");
		Assert.assertTrue(l.isValid());
		Assert.assertEquals("1238.5", ""+l.getValue());
		

		
		float f = 0.5f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = 32.5f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = -32.5f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = -0.5f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.01p0f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.fffffep0f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = -0x1.fffffep0f;
		Assert.assertEquals(f, new FloatLiteral(0, 0, FloatLiteral.createExactHexLiteral(f)).getValue());
		
		f = 0x1.433334p3f;
		Assert.assertEquals("1.433334p3", FloatLiteral.createExactHexLiteral(f));
		
		f = -0x1.433334p3f;
		Assert.assertEquals("-1.433334p3", FloatLiteral.createExactHexLiteral(f));
	}
	

}
