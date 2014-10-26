package org.jasm.test.parser;

import org.jasm.JasmConsts;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.InvokeInterfaceInstruction;
import org.jasm.item.instructions.LdcInstruction;
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
		
		Assert.assertEquals(8, code.getInstructions().getSize());
		
		Assert.assertTrue(code.getInstructions().get(0).getOpCode() == OpCodes.nop);
		Assert.assertSame(code.getInstructions().get(0), code.getInstructions().checkAndLoadFromSymbolTable(new SymbolReference(0, 0, "label1")));
		Assert.assertTrue(code.getInstructions().get(1).getOpCode() == OpCodes.nop);
		Assert.assertTrue(code.getInstructions().get(2).getOpCode() == OpCodes.ldc);
		LdcInstruction ldc = (LdcInstruction)code.getInstructions().get(2);
		Assert.assertSame(ldc.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(ClassInfo.class, new SymbolReference(0, 0, "classref_0")));
		Assert.assertTrue(code.getInstructions().get(3).getOpCode() == OpCodes.pop);
		Assert.assertTrue(code.getInstructions().get(4).getOpCode() == OpCodes.getfield);
		ConstantPoolInstruction getfield = (ConstantPoolInstruction)code.getInstructions().get(4);
		Assert.assertSame(getfield.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(FieldrefInfo.class, new SymbolReference(0, 0, "fieldref_13")));
		Assert.assertTrue(code.getInstructions().get(5).getOpCode() == OpCodes.pop);
		Assert.assertTrue(code.getInstructions().get(6).getOpCode() == OpCodes.invokeinterface);
		InvokeInterfaceInstruction invi = (InvokeInterfaceInstruction)code.getInstructions().get(6);
		Assert.assertSame(invi.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(InterfaceMethodrefInfo.class, new SymbolReference(0, 0, "intfmethodref_92")));
		Assert.assertEquals(2, invi.getCount());
		Assert.assertTrue(code.getInstructions().get(7).getOpCode() == OpCodes.return_);
		
	}

}
