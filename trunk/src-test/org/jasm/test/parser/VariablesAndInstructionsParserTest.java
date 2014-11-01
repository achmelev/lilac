package org.jasm.test.parser;

import org.jasm.JasmConsts;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.instructions.BipushInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.InvokeInterfaceInstruction;
import org.jasm.item.instructions.LdcInstruction;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.LocalVariablesPool;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.ShortLocalVariableInstruction;
import org.jasm.item.instructions.SipushInstruction;
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
		
		LocalVariable var = pool.checkAndLoad(null,new SymbolReference(0, 0, "o1"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "d1"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		Assert.assertNotNull(var);
		Assert.assertEquals(1, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "f1"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		Assert.assertNotNull(var);
		Assert.assertEquals(2, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "i1"), JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "l1"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "r1"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		Assert.assertEquals(20, code.getInstructions().getSize());
		
		Assert.assertTrue(code.getInstructions().get(0).getOpCode() == OpCodes.nop);
		Assert.assertSame(code.getInstructions().get(0), code.getInstructions().checkAndLoadFromSymbolTable(null,new SymbolReference(0, 0, "label1")));
		Assert.assertTrue(code.getInstructions().get(1).getOpCode() == OpCodes.nop);
		Assert.assertTrue(code.getInstructions().get(2).getOpCode() == OpCodes.ldc);
		LdcInstruction ldc = (LdcInstruction)code.getInstructions().get(2);
		Assert.assertSame(ldc.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(null,ClassInfo.class, new SymbolReference(0, 0, "classref_0")));
		Assert.assertTrue(code.getInstructions().get(3).getOpCode() == OpCodes.pop);
		Assert.assertTrue(code.getInstructions().get(4).getOpCode() == OpCodes.getfield);
		ConstantPoolInstruction getfield = (ConstantPoolInstruction)code.getInstructions().get(4);
		Assert.assertSame(getfield.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(null,FieldrefInfo.class, new SymbolReference(0, 0, "fieldref_13")));
		Assert.assertTrue(code.getInstructions().get(5).getOpCode() == OpCodes.pop);
		Assert.assertTrue(code.getInstructions().get(6).getOpCode() == OpCodes.invokeinterface);
		InvokeInterfaceInstruction invi = (InvokeInterfaceInstruction)code.getInstructions().get(6);
		Assert.assertSame(invi.getConstantReferences()[0],clazz.getConstantPool().checkAndLoadFromSymbolTable(null,InterfaceMethodrefInfo.class, new SymbolReference(0, 0, "intfmethodref_92")));
		Assert.assertEquals(2, invi.getCount());
		
		Assert.assertEquals(code.getInstructions().get(7).getOpCode(), OpCodes.aload_0);
		Assert.assertEquals(code.getInstructions().get(8).getOpCode(), OpCodes.astore_0);
		
		Assert.assertEquals(code.getInstructions().get(9).getOpCode(), OpCodes.dload);
		Assert.assertEquals(code.getInstructions().get(10).getOpCode(), OpCodes.dstore);
		LocalVariableInstruction dload = (LocalVariableInstruction)code.getInstructions().get(9);
		Assert.assertEquals(1, dload.getLocalVariableIndex());
		LocalVariableInstruction dstore = (LocalVariableInstruction)code.getInstructions().get(10);
		Assert.assertEquals(1, dstore.getLocalVariableIndex());
		
		Assert.assertEquals(code.getInstructions().get(11).getOpCode(), OpCodes.fload);
		Assert.assertEquals(code.getInstructions().get(12).getOpCode(), OpCodes.fstore);
		LocalVariableInstruction fload = (LocalVariableInstruction)code.getInstructions().get(11);
		Assert.assertEquals(2, fload.getLocalVariableIndex());
		Assert.assertTrue(fload.isWide());
		LocalVariableInstruction fstore = (LocalVariableInstruction)code.getInstructions().get(12);
		Assert.assertEquals(2, fstore.getLocalVariableIndex());
		Assert.assertTrue(fstore.isWide());
		
		Assert.assertEquals(code.getInstructions().get(13).getOpCode(), OpCodes.lload);
		Assert.assertEquals(code.getInstructions().get(14).getOpCode(), OpCodes.lstore);
		LocalVariableInstruction lload = (LocalVariableInstruction)code.getInstructions().get(13);
		Assert.assertEquals(5, lload.getLocalVariableIndex());
		LocalVariableInstruction lstore = (LocalVariableInstruction)code.getInstructions().get(14);
		Assert.assertEquals(5, lstore.getLocalVariableIndex());
		
		Assert.assertEquals(code.getInstructions().get(15).getOpCode(), OpCodes.bipush);
		BipushInstruction bipush = (BipushInstruction)code.getInstructions().get(15);
		Assert.assertEquals(100, bipush.getValue());
		
		Assert.assertEquals(code.getInstructions().get(16).getOpCode(), OpCodes.sipush);
		SipushInstruction sipush = (SipushInstruction)code.getInstructions().get(16);
		Assert.assertEquals(-260, sipush.getValue());
		
		Assert.assertTrue(code.getInstructions().get(19).getOpCode() == OpCodes.return_);
		
	}

}
