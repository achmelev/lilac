package org.jasm.test.parser;

import org.jasm.JasmConsts;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.DebugLocalVariable;
import org.jasm.item.attribute.ExceptionHandler;
import org.jasm.item.attribute.LineNumber;
import org.jasm.item.attribute.LineNumberTableAttributeContent;
import org.jasm.item.attribute.LocalVariableTableAttributeContent;
import org.jasm.item.attribute.StackMapBinaryAttributeContent;
import org.jasm.item.attribute.UnknownAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.instructions.BipushInstruction;
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.IincInstruction;
import org.jasm.item.instructions.InvokeInterfaceInstruction;
import org.jasm.item.instructions.LdcInstruction;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.LocalVariablesPool;
import org.jasm.item.instructions.LookupSwitchInstruction;
import org.jasm.item.instructions.MultianewarrayInstruction;
import org.jasm.item.instructions.NewarrayInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.item.instructions.TableSwitchInstruction;
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
		Clazz clazz = doTest();
		
		
		Assert.assertNotNull(clazz);
		
		Method m = clazz.getMethods().getMethod("method", "()Ljava/lang/Object;");
		Assert.assertNotNull(m);
		
		byte [] data = ((UnknownAttributeContent)m.getAttributes().get(0).getContent()).getData();
		Assert.assertEquals("Mein Attribut",new String(data));
		Assert.assertEquals("MyAttr",m.getAttributes().get(0).getName().getValue());
		
		
		CodeAttributeContent code = (CodeAttributeContent)m.getAttributes().get(2).getContent();
		
		data = ((UnknownAttributeContent)code.getAttributes().get(0).getContent()).getData();
		Assert.assertEquals("Mein Attribut",new String(data));
		Assert.assertEquals("MyAttr",code.getAttributes().get(0).getName().getValue());
				
		LocalVariablesPool pool = code.getInstructions().getVariablesPool();
		
		Assert.assertEquals(8, pool.calculateSize());
		
		LocalVariable var = pool.checkAndLoad(null,new SymbolReference(0, 0, "o1"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "d1"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(1, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "f1"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(2, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "i1"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "l1"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var = pool.checkAndLoad(null,new SymbolReference(0, 0, "r1"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		Assert.assertEquals(51, code.getInstructions().getSize());
		
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
		
		Assert.assertEquals(code.getInstructions().get(19).getOpCode(), OpCodes.iinc);
		IincInstruction iinc = (IincInstruction)code.getInstructions().get(19);
		Assert.assertEquals(100, iinc.getValue());
		
		Assert.assertEquals(code.getInstructions().get(20).getOpCode(), OpCodes.iinc);
		iinc = (IincInstruction)code.getInstructions().get(20);
		Assert.assertEquals(-260, iinc.getValue());
		
		Assert.assertEquals(code.getInstructions().get(22).getOpCode(), OpCodes.newarray);
		NewarrayInstruction newarray = (NewarrayInstruction)code.getInstructions().get(22);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_BOOLEAN, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(24).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(24);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_BYTE, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(26).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(26);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_CHAR, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(28).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(28);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_DOUBLE, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(30).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(30);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_FLOAT, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(32).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(32);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_INT, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(34).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(34);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_LONG, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(36).getOpCode(), OpCodes.newarray);
		newarray = (NewarrayInstruction)code.getInstructions().get(36);
		Assert.assertEquals(JasmConsts.ARRAY_TYPE_SHORT, newarray.getType());
		
		Assert.assertEquals(code.getInstructions().get(44).getOpCode(), OpCodes.multianewarray);
		MultianewarrayInstruction multinewarray = (MultianewarrayInstruction)code.getInstructions().get(44);
		Assert.assertEquals("java/lang/Object", multinewarray.getClassInfo().getClassName());
		Assert.assertEquals(3, multinewarray.getDimensions());
		
		Assert.assertEquals(code.getInstructions().get(45).getOpCode(), OpCodes.lookupswitch);
		LookupSwitchInstruction lookupswitch = (LookupSwitchInstruction)code.getInstructions().get(45);
		Assert.assertEquals(OpCodes.tableswitch, lookupswitch.getDefaultTarget().getOpCode());
		Assert.assertEquals(3, lookupswitch.getValues().length);
		Assert.assertEquals(-1, lookupswitch.getValues()[0]);
		Assert.assertEquals(10, lookupswitch.getValues()[1]);
		Assert.assertEquals(3, lookupswitch.getValues()[2]);
		Assert.assertEquals(3, lookupswitch.getTargets().length);
		Assert.assertEquals(OpCodes.aload_0, lookupswitch.getTargets()[0].getOpCode());
		Assert.assertEquals(OpCodes.newarray, lookupswitch.getTargets()[1].getOpCode());
		Assert.assertEquals(OpCodes.multianewarray, lookupswitch.getTargets()[2].getOpCode());
		
		Assert.assertEquals(code.getInstructions().get(46).getOpCode(), OpCodes.tableswitch);
		TableSwitchInstruction tableswitch = (TableSwitchInstruction)code.getInstructions().get(46);
		Assert.assertEquals(OpCodes.goto_, tableswitch.getDefaultTarget().getOpCode());
		Assert.assertEquals(-1, tableswitch.getLow());
		Assert.assertEquals(1, tableswitch.getHigh());
		Assert.assertEquals(3, tableswitch.getTargets().length);
		Assert.assertEquals(OpCodes.aload_0, tableswitch.getTargets()[0].getOpCode());
		Assert.assertEquals(OpCodes.newarray, tableswitch.getTargets()[1].getOpCode());
		Assert.assertEquals(OpCodes.multianewarray, tableswitch.getTargets()[2].getOpCode());
		
		Assert.assertEquals(code.getInstructions().get(47).getOpCode(), OpCodes.goto_);
		BranchInstruction goto_ = (BranchInstruction)code.getInstructions().get(47);
		Assert.assertEquals(OpCodes.areturn, goto_.getTargetInst().getOpCode());
		
		Assert.assertTrue(code.getInstructions().get(50).getOpCode() == OpCodes.areturn);
		
		Assert.assertEquals(2, code.getExceptionTable().getSize());
		ExceptionHandler handler = code.getExceptionTable().get(0);
		
		Assert.assertEquals(OpCodes.ldc, handler.getStartInstruction().getOpCode());
		Assert.assertEquals(OpCodes.bipush, handler.getEndInstruction().getOpCode());
		Assert.assertEquals(OpCodes.nop, handler.getHandlerInstruction().getOpCode());
		Assert.assertEquals("java/lang/RuntimeException", handler.getCatchType().getClassName());
		
		handler = code.getExceptionTable().get(1);
		Assert.assertEquals(OpCodes.nop, handler.getStartInstruction().getOpCode());
		Assert.assertEquals(OpCodes.nop, handler.getEndInstruction().getOpCode());
		Assert.assertEquals(OpCodes.areturn, handler.getHandlerInstruction().getOpCode());
		Assert.assertNull(handler.getCatchType());
		
		LineNumberTableAttributeContent linenumbers = (LineNumberTableAttributeContent)code.getAttributes().get(1).getContent();
		Assert.assertEquals(2, linenumbers.getSize());
		
		LineNumber line1 = linenumbers.get(0);
		Assert.assertEquals(10, line1.getLineNumber());
		Assert.assertEquals(OpCodes.nop, line1.getStartInstruction().getOpCode());
		
		line1 = linenumbers.get(1);
		Assert.assertEquals(20, line1.getLineNumber());
		Assert.assertEquals(OpCodes.ldc, line1.getStartInstruction().getOpCode());
		
		LocalVariableTableAttributeContent debugvars = (LocalVariableTableAttributeContent)code.getAttributes().get(2).getContent();
		Assert.assertEquals(2, debugvars.getSize());
		DebugLocalVariable dvar = debugvars.get(0);
		Assert.assertEquals("o1",dvar.getVariable().getName().getContent());
		Assert.assertEquals(OpCodes.nop,dvar.getStartInstruction().getOpCode());
		Assert.assertNull(dvar.getEndIndsruction());
		Assert.assertEquals("this",dvar.getName().getValue());
		Assert.assertEquals("Lorg/jasm/test/testclass/AbstractGenericClass;",dvar.getDescriptor().getValue());
		
		dvar = debugvars.get(1);
		Assert.assertEquals("d1",dvar.getVariable().getName().getContent());
		Assert.assertEquals(OpCodes.nop,dvar.getStartInstruction().getOpCode());
		Assert.assertEquals(OpCodes.areturn,dvar.getEndIndsruction().getOpCode());
		Assert.assertEquals("doubleVar",dvar.getName().getValue());
		Assert.assertEquals("D",dvar.getDescriptor().getValue());
	}

}
