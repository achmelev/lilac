package org.jasm.test.parser;

import org.jasm.JasmConsts;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariablesPool;
import org.jasm.item.instructions.OpCodes;
import org.jasm.parser.literals.SymbolReference;
import org.junit.Assert;
import org.junit.Test;



public class VariablesAndInstructionsParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "VariablesAndInstructions.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = parse();
		if (parser.getErrorMessages().size() > 0) {
			parser.debugErrors();
			Assert.fail("Parsing failed!");
		} 
		
		Assert.assertNotNull(clazz);
		
		Method m = clazz.getMethods().getMethod("method", "()Ljava/lang/Object;");
		Assert.assertNotNull(m);
		
		CodeAttributeContent code = (CodeAttributeContent)m.getAttributes().get(1).getContent();
		LocalVariablesPool pool = code.getInstructions().getVariablesPool();
		
		Assert.assertEquals(8, pool.calculateSize());
		
		LocalVariable var = pool.checkAndLoad(new SymbolReference(0, 0, "o1"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var = pool.checkAndLoad(new SymbolReference(0, 0, "d1"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		Assert.assertNotNull(var);
		Assert.assertEquals(1, var.getIndex());
		
		var = pool.checkAndLoad(new SymbolReference(0, 0, "f1"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		Assert.assertNotNull(var);
		Assert.assertEquals(2, var.getIndex());
		
		var = pool.checkAndLoad(new SymbolReference(0, 0, "i1"), JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var = pool.checkAndLoad(new SymbolReference(0, 0, "l1"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var = pool.checkAndLoad(new SymbolReference(0, 0, "r1"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		Assert.assertTrue(code.getInstructions().get(0).getOpCode() == OpCodes.nop);
		Assert.assertSame(code.getInstructions().get(0), code.getInstructions().checkAndLoadFromSymbolTable(new SymbolReference(0, 0, "label1")));
		Assert.assertTrue(code.getInstructions().get(1).getOpCode() == OpCodes.nop);
		Assert.assertTrue(code.getInstructions().get(2).getOpCode() == OpCodes.return_);
		
	}

}
