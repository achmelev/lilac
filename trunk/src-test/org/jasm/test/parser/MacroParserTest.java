package org.jasm.test.parser;

import junit.framework.Assert;

import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.macros.MacroCall;
import org.jasm.parser.literals.ClassReference;
import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.LongLiteral;
import org.jasm.parser.literals.NullLiteral;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class MacroParserTest extends AbstractParserTestCase {
	
	
	@Override
	protected String getDateiName() {
		return "Macro.jasm";
	}

	
	@Test
	public void test() {
		Clazz clazz = doTest();
		Assert.assertNotNull(clazz);
		
		CodeAttributeContent code = (CodeAttributeContent)clazz.getMethods().getMethod("calculate", "()I").
		getAttributes().
		getAttributesByContentType(CodeAttributeContent.class).
		get(0).getContent();
		
		Instructions instrs = code.getInstructions();
		Assert.assertEquals(1, instrs.getMacrocalls().size());
		
		MacroCall call = instrs.getMacrocalls().get(0);
		Assert.assertEquals("test.argumentlessmul", call.getNameReference().getSymbolName());
		Assert.assertEquals("macro", call.getSymbolName());
		Assert.assertEquals(1, call.getIndex());
		Assert.assertEquals(5, call.getArguments().size());
		Assert.assertEquals(10L,((LongLiteral)call.getArguments().get(0)).getValue());
		Assert.assertEquals(15.0,((DoubleLiteral)call.getArguments().get(1)).getValue());
		Assert.assertEquals("20",((StringLiteral)call.getArguments().get(2)).getStringValue());
		Assert.assertEquals("i30",((SymbolReference)call.getArguments().get(3)).getSymbolName());
		Assert.assertTrue(call.getArguments().get(4) instanceof NullLiteral);

		
		
		Assert.assertEquals(5, instrs.getSize());
		
		
		
	}

	
	

}
