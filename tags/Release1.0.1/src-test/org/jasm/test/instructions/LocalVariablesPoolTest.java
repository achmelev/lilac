package org.jasm.test.instructions;

import org.jasm.JasmConsts;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariablesPool;
import org.jasm.parser.literals.SymbolReference;
import org.junit.Test;

import junit.framework.Assert;

public class LocalVariablesPoolTest {
	
	@Test
	public void test() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(8, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(2, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(6, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, true);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		
		
	}
	
	@Test
	public void test2() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(9, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(8, var.getIndex());
		
		
		
	}
	
	@Test
	public void test3() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar2"));
		var.setParentName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(9, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(8, var.getIndex());
		
		
		
	}
	
	@Test
	public void test4() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar2"));
		var.setParentName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar2"));
		var.setParentName(new SymbolReference(0, 0, "refVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(13, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(10, var.getIndex());
		
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(12, var.getIndex());
		
		
		
	}
	
	@Test
	public void test5() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar2"));
		var.setParentName(new SymbolReference(0, 0, "XXX"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar3"));
		var.setParentName(new SymbolReference(0, 0, "intVar2"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar2"));
		var.setParentName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar2"));
		var.setParentName(new SymbolReference(0, 0, "refVar"));
		var.setOffset(3);
		pool.addVariable(var);
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(13, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(10, var.getIndex());
		
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(12, var.getIndex());
		
		
		
	}
	
	@Test
	public void test7() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar2"));
		var.setParentName(new SymbolReference(0, 0, "XXX"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar3"));
		var.setParentName(new SymbolReference(0, 0, "intVar2"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar2"));
		var.setParentName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar2"));
		var.setParentName(new SymbolReference(0, 0, "refVar"));
		var.setOffset(3);
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar2"));
		var.setParentName(new SymbolReference(0, 0, "retVar3"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar3"));
		var.setParentName(new SymbolReference(0, 0, "retVar2"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(13, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(10, var.getIndex());
		
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(12, var.getIndex());
		
		
		
	}
	
	@Test
	public void test6() {
		
		LocalVariablesPool pool = new LocalVariablesPool();
		pool.setEmittingDeactivated(true);
		
		LocalVariable var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT);
		var.setName(new SymbolReference(0, 0, "floatVar"));
		var.setOffset(3);
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar2"));
		var.setParentName(new SymbolReference(0, 0, "XXX"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar2"));
		var.setParentName(new SymbolReference(0, 0, "XXX"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_INT);
		var.setName(new SymbolReference(0, 0, "intVar3"));
		var.setParentName(new SymbolReference(0, 0, "intVar2"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_LONG);
		var.setName(new SymbolReference(0, 0, "longVar2"));
		var.setParentName(new SymbolReference(0, 0, "longVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE);
		var.setName(new SymbolReference(0, 0, "refVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE);
		var.setName(new SymbolReference(0, 0, "doubleVar2"));
		var.setParentName(new SymbolReference(0, 0, "refVar"));
		var.setOffset(3);
		pool.addVariable(var);
		
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar2"));
		var.setParentName(new SymbolReference(0, 0, "retVar3"));
		pool.addVariable(var);
		
		var = new LocalVariable(JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS);
		var.setName(new SymbolReference(0, 0, "retVar3"));
		var.setParentName(new SymbolReference(0, 0, "retVar2"));
		pool.addVariable(var);
		
		pool.resolveAfterParse();
		Assert.assertEquals(13, pool.calculateSize());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(0, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "floatVar"), JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(3, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "intVar"), JasmConsts.LOCAL_VARIABLE_TYPE_INT, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(4, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "longVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_LONG, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(5, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "refVar"), JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(7, var.getIndex());
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "doubleVar2"), JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(10, var.getIndex());
		
		
		var =  pool.checkAndLoad(null,new SymbolReference(0, 0, "retVar"), JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS, false);
		Assert.assertNotNull(var);
		Assert.assertEquals(12, var.getIndex());
		
		
		
	}

}
