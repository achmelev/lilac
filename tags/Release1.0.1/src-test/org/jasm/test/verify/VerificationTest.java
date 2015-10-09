package org.jasm.test.verify;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.instructions.verify.AppendFrame;
import org.jasm.item.instructions.verify.ChopFrame;
import org.jasm.item.instructions.verify.Frame;
import org.jasm.item.instructions.verify.FullFrame;
import org.jasm.item.instructions.verify.SameLocalsOneStackItemFrame;
import org.jasm.item.instructions.verify.SameFrame;
import org.jasm.item.instructions.verify.error.InconsistentStackSizeException;
import org.jasm.item.instructions.verify.error.StackOverflowException;
import org.jasm.item.instructions.verify.error.StackmapAppendOverflowException;
import org.jasm.item.instructions.verify.error.StackmapChopUnderflowException;
import org.jasm.item.instructions.verify.error.StackmapFullLocalsOverflowException;
import org.jasm.item.instructions.verify.error.StackmapFullStackOverflowException;
import org.jasm.item.instructions.verify.error.StackmapSameLocalsStackOverflowException;
import org.jasm.item.instructions.verify.error.UnexpectedRegisterTypeException;
import org.jasm.item.instructions.verify.error.UnexpectedStackTypeException;
import org.jasm.item.instructions.verify.error.UnknownClassException;
import org.jasm.item.instructions.verify.types.IClassQuery;
import org.jasm.item.instructions.verify.types.ObjectValueType;
import org.jasm.item.instructions.verify.types.UninitializedValueType;
import org.jasm.item.instructions.verify.types.VerificationType;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;
import org.junit.Assert;
import org.junit.Test;

public class VerificationTest implements IClassQuery {
	
	@Test
	public void testSimpleTypeAssign() {
		Assert.assertTrue(VerificationType.DOUBLE.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.DOUBLE.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertTrue(VerificationType.FLOAT.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.FLOAT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertTrue(VerificationType.INT.isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.INT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.INT));
		Assert.assertTrue(VerificationType.LONG.isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.LONG.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"),this)));
		
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.LONG));
		Assert.assertTrue(VerificationType.NULL.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.NULL.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.INT));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.LONG));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.NULL));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.TOP));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertTrue(VerificationType.TOP.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.TOP));
		Assert.assertTrue(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(VerificationType.UNINITIALIZED_THIS.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.LONG));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertTrue(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 10)));
		Assert.assertFalse(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
	
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.DOUBLE));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.FLOAT));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.INT));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.LONG));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.NULL));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.TOP));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(VerificationType.UNINITIALIZED_THIS));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).isAssignableFrom(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		
	}
	
	@Test
	public void testSimpleTypeMerge() {
		Assert.assertEquals(VerificationType.DOUBLE, VerificationType.DOUBLE.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, VerificationType.DOUBLE.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.FLOAT, VerificationType.FLOAT.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, VerificationType.FLOAT.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.INT, VerificationType.INT.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, VerificationType.INT.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.LONG, VerificationType.LONG.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, VerificationType.LONG.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.NULL, VerificationType.NULL.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.NULL.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this), VerificationType.NULL.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.UNINITIALIZED_THIS, VerificationType.UNINITIALIZED_THIS.mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, VerificationType.UNINITIALIZED_THIS.mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.LONG));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 10)));
		Assert.assertEquals(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0), new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		Assert.assertEquals(VerificationType.TOP, new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.DOUBLE));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.FLOAT));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.INT));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.LONG));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this), new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.NULL));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.TOP));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(VerificationType.UNINITIALIZED_THIS));
		Assert.assertEquals(VerificationType.TOP, new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this).mergeWith(new UninitializedValueType(new TypeDescriptor("Ljava/lang/String;"), 0)));
		
	}
	
	@Test
	public void testObjectTypeAssignable() {
		Assert.assertTrue(VerificationType.OBJECT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertTrue(VerificationType.OBJECT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertTrue(VerificationType.OBJECT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/lang/String;"), this)));
		Assert.assertTrue(VerificationType.OBJECT.isAssignableFrom(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/lang/String;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/lang/String;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[I"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[I"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[I"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/lang/String;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("[I"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[I"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[I"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[J"), this)));
		
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/lang/String;"), this)));
		Assert.assertTrue(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[I"), this)));
		Assert.assertFalse(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"), this).isAssignableFrom(new ObjectValueType(new TypeDescriptor("[J"), this)));
		
		
	}
	
	@Test
	public void testObjectTypeMerge() {
		Assert.assertEquals(VerificationType.OBJECT, VerificationType.OBJECT.cloneWithQuery(this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, VerificationType.OBJECT.cloneWithQuery(this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, VerificationType.OBJECT.cloneWithQuery(this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, VerificationType.OBJECT.cloneWithQuery(this).mergeWith(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[I"), this)));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("Ljava/io/Reader;"), this), new ObjectValueType(new TypeDescriptor("Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/io/PushbackReader;"), this)));
		
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[I"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[I"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[I"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("[I"), this), new ObjectValueType(new TypeDescriptor("[I"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[I"), this)));
		
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Object;"),this), new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/lang/Runnable;"), this)));
		Assert.assertEquals(VerificationType.OBJECT, new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[I"), this)));
		Assert.assertEquals(new ObjectValueType(new TypeDescriptor("[Ljava/io/Reader;"),this), new ObjectValueType(new TypeDescriptor("[Ljava/io/LineNumberReader;"), this).mergeWith(new ObjectValueType(new TypeDescriptor("[Ljava/io/PushbackReader;"), this)));
	}
	
	@Test
	public void copyFrameTest() {
		Frame frame = new Frame(10, 5);
		frame.push(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this));
		frame.push(VerificationType.LONG);
		frame.push(VerificationType.LONG);
		
		Assert.assertTrue(frame.copy().equals(frame));
	}
	
	@Test
	public void frameOperationsTest() {
		Frame frame = new Frame(10, 5);
		frame.push(VerificationType.INT);
		frame.push(VerificationType.FLOAT);
		frame.push(VerificationType.LONG);
		Assert.assertEquals(VerificationType.INT, frame.getTypeOnStack(0));
		Assert.assertEquals(VerificationType.FLOAT, frame.getTypeOnStack(1));
		Assert.assertEquals(VerificationType.LONG, frame.getTypeOnStack(2));
		Assert.assertEquals(frame.getCurrentStackSize(), 4);
		for (int i=0;i<5; i++) {
			frame.getTypeInRegister(i).equals(VerificationType.TOP);
		}
		Assert.assertEquals(0, frame.getActiveLocals());
		
		try {
			frame.push(VerificationType.DOUBLE);
			Assert.fail("Expected exception!");
		} catch (StackOverflowException e) {
			//
		} 
		
		try {
			frame.pop(VerificationType.INT);
			Assert.fail("Expected exception!");
		} catch (UnexpectedStackTypeException e) {
			//
		}
		
		frame.pop(VerificationType.LONG);
		Assert.assertEquals(frame.getCurrentStackSize(), 2);
		frame.push(VerificationType.UNINITIALIZED_THIS);
		
		frame.store(VerificationType.UNINITIALIZED_THIS,3);
		
		for (int i=0;i<10; i++) {
			if (i==3) {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.UNINITIALIZED_THIS);
			} else {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.TOP);
			}
			
		}
		
		Assert.assertEquals(4, frame.getActiveLocals());
		
		frame.push(VerificationType.DOUBLE);
		
		try {
			frame.store(VerificationType.LONG, 2);
			Assert.fail("Expected exception!");
		} catch (UnexpectedStackTypeException e) {
			//
		}
		
		frame.store(VerificationType.DOUBLE, 2);
		
		
		for (int i=0;i<10; i++) {
			if (i==2) {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.DOUBLE);
			} else {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.TOP);
			}
			
		}
		
		Assert.assertEquals(4, frame.getActiveLocals());
		
		frame.store(VerificationType.FLOAT, 3);
		
		for (int i=0;i<10; i++) {
			if (i==3) {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.FLOAT);
			} else {
				Assert.assertEquals(frame.getTypeInRegister(i),VerificationType.TOP);
			}
			
		}
		
		Assert.assertEquals(4, frame.getActiveLocals());
		
		try {
			frame.load(VerificationType.INT, 3);
			Assert.fail("Expected exception!");
		} catch (UnexpectedRegisterTypeException e) {
			//
		}
		
		frame.load(VerificationType.FLOAT, 3);
		Assert.assertEquals(frame.getCurrentStackSize(), 2);
		Assert.assertEquals(VerificationType.INT, frame.getTypeOnStack(0));
		Assert.assertEquals(VerificationType.FLOAT, frame.getTypeOnStack(1));
		
		
	}
	
	@Test
	public void isAssignableFrameTest() {
		Frame frame1 = new Frame(10,10);
		Frame frame2 = new Frame(10,10);
		
		Assert.assertTrue(frame1.isAssignableFrom(frame2));
		
		frame1.push(VerificationType.INT);
		frame1.push(VerificationType.DOUBLE);
		frame1.push(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this));
		
		try {
			frame1.isAssignableFrom(frame2);
			Assert.fail("Expected exception!");
		} catch (InconsistentStackSizeException e) {
			
		}
		
		frame2.push(VerificationType.INT);
		frame2.push(VerificationType.DOUBLE);
		frame2.push(VerificationType.LONG);
		
		try {
			frame1.isAssignableFrom(frame2);
			Assert.fail("Expected exception!");
		} catch (UnexpectedStackTypeException e) {
			
		}
		
		frame2.pop(VerificationType.LONG);
		
		frame2.push(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this));
		
		Assert.assertTrue(frame1.isAssignableFrom(frame2));
		
		frame1.push(VerificationType.INT);
		frame1.store(VerificationType.INT, 1);
		
		try {
			frame1.isAssignableFrom(frame2);
			Assert.fail("Expected exception!");
		} catch (UnexpectedRegisterTypeException e) {
			
		}
		
		frame2.push(VerificationType.INT);
		frame2.store(VerificationType.INT, 1);
		
		Assert.assertTrue(frame1.isAssignableFrom(frame2));
		
		frame2.push(VerificationType.DOUBLE);
		frame2.store(VerificationType.DOUBLE, 2);
		
		Assert.assertTrue(frame1.isAssignableFrom(frame2));
		
	}
	
	@Test
	public void mergeFrameTest() {
		
		
		List<VerificationType> vars12 = new ArrayList<VerificationType>();
		vars12.add(VerificationType.INT);
		vars12.add(VerificationType.DOUBLE);
		vars12.add(VerificationType.TOP);
		vars12.add(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this));
		vars12.add(new UninitializedValueType(new TypeDescriptor("Ljava/lang/Runnable;"), 5));
		vars12.add(VerificationType.UNINITIALIZED_THIS);
		vars12.add(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this));
		vars12.add(VerificationType.LONG);
		vars12.add(VerificationType.TOP);
		vars12.add(VerificationType.FLOAT);
		
		List<VerificationType> vars2 = new ArrayList<VerificationType>();
		vars2.add(VerificationType.INT);
		vars2.add(VerificationType.LONG);
		vars2.add(VerificationType.TOP);
		vars2.add(new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), this));
		vars2.add(new UninitializedValueType(new TypeDescriptor("Ljava/lang/Runnable;"), 2));
		vars2.add(VerificationType.UNINITIALIZED_THIS);
		vars2.add(VerificationType.NULL);
		vars2.add(VerificationType.TOP);
		vars2.add(VerificationType.TOP);
		vars2.add(VerificationType.TOP);
		
		List<VerificationType> vars3 = new ArrayList<VerificationType>();
		vars3.add(VerificationType.INT);
		vars3.add(VerificationType.TOP);
		vars3.add(VerificationType.TOP);
		vars3.add(new ObjectValueType(new TypeDescriptor("Ljava/lang/Object;"), this));
		vars3.add(VerificationType.TOP);
		vars3.add(VerificationType.UNINITIALIZED_THIS);
		vars3.add(new ObjectValueType(new TypeDescriptor("Ljava/lang/Runnable;"), this));
		vars3.add(VerificationType.TOP);
		vars3.add(VerificationType.TOP);
		vars3.add(VerificationType.TOP);
		
		Frame frame1 = Frame.createFrame(vars12, vars12, 12);
		Frame frame2 = Frame.createFrame(vars2, vars2, 12);
		Frame frame3 = Frame.createFrame(vars3, vars3, 12);
		
		Assert.assertTrue(frame1.merge(frame2).equals(frame3));
		Assert.assertEquals(7, frame1.merge(frame2).getActiveLocals());
		
		
		
		
		
	}
	
	@Test
	public void initialFrameTest() {
		String descriptor = "()V";
		List<VerificationType> stack = new ArrayList<VerificationType>(); 
		List<VerificationType> locals = new ArrayList<VerificationType>();
		locals.add(new ObjectValueType("Lorg/jasm/test/verify/Dummy;", this));
		Frame frame1 = Frame.createInitialFrame("org/jasm/test/verify/Dummy", false, false, 1, 1, new MethodDescriptor(descriptor), this);
		Frame frame2 = Frame.createFrame(locals, stack, 1);
		Assert.assertTrue(frame1.equals(frame2));
		
		descriptor = "()V";
		locals = new ArrayList<VerificationType>();
		frame1 = Frame.createInitialFrame("org/jasm/test/verify/Dummy", false, true, 0, 1, new MethodDescriptor(descriptor), this);
		frame2 = Frame.createFrame(locals, stack, 1);
		Assert.assertTrue(frame1.equals(frame2));
		
		descriptor = "(BLjava/lang/Runnable;)V";
		locals = new ArrayList<VerificationType>();
		locals.add(new ObjectValueType("Lorg/jasm/test/verify/Dummy;", this));
		locals.add(VerificationType.INT);
		locals.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		frame1 = Frame.createInitialFrame("org/jasm/test/verify/Dummy", false, false, 5, 1, new MethodDescriptor(descriptor), this);
		frame2 = Frame.createFrame(locals, stack, 1);
		Assert.assertTrue(frame1.equals(frame2));
		
		descriptor = "(BLjava/lang/Runnable;)V";
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.INT);
		locals.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		frame1 = Frame.createInitialFrame("org/jasm/test/verify/Dummy", false, true, 2, 1, new MethodDescriptor(descriptor), this);
		frame2 = Frame.createFrame(locals, stack, 1);
		Assert.assertTrue(frame1.equals(frame2));
		
		descriptor = "(DLjava/lang/Runnable;)V";
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.UNINITIALIZED_THIS);
		locals.add(VerificationType.DOUBLE);
		locals.add(VerificationType.TOP);
		locals.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		frame1 = Frame.createInitialFrame("org/jasm/test/verify/Dummy", true, false, 4, 1, new MethodDescriptor(descriptor), this);
		frame2 = Frame.createFrame(locals, stack, 1);
		Assert.assertTrue(frame1.equals(frame2));
		
	}
	
	@Test
	public void applyStackMapTest() {
		List<VerificationType> locals = new ArrayList<VerificationType>();
		for (int i=0;i<5; i++) {
			locals.add(VerificationType.TOP);
		}
		List<VerificationType> stack = new ArrayList<VerificationType>();
		
		Frame frame = Frame.createFrame(locals, stack, 5);
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.INT);
		locals.add(VerificationType.NULL);
		locals.add(VerificationType.DOUBLE);
	
		
		try {
			frame.applyStackmapAppend(locals);
			Assert.fail("Expected exception!");
		} catch (StackmapAppendOverflowException e) {
			//ignore
		}
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.INT);
		locals.add(VerificationType.DOUBLE);
		frame = frame.applyStackmapAppend(locals);
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.INT);
		locals.add(VerificationType.DOUBLE);
		locals.add(VerificationType.TOP);
		Frame result = Frame.createFrame(locals, new ArrayList<VerificationType>(), 5);
		
		Assert.assertTrue(result.equals(frame));
		
		try {
			frame.applyStackmapChop(6);
			Assert.fail("Expected exception!");
		} catch (StackmapChopUnderflowException e) {
			//ignore
		}
		
		frame = frame.applyStackmapChop(2);
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		
		result = Frame.createFrame(locals, new ArrayList<VerificationType>(), 5);
		
		Assert.assertTrue(result.equals(frame));
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.INT);
		locals.add(VerificationType.DOUBLE);
		locals.add(VerificationType.NULL);
		
		stack = new ArrayList<VerificationType>();
		stack.add(VerificationType.LONG);
		stack.add(VerificationType.INT);
		stack.add(VerificationType.DOUBLE);
		
		try {
			frame = frame.applyStackmapFull(locals, stack);
			Assert.fail("Expected exception!");
		} catch (StackmapFullLocalsOverflowException e) {
			//ignore
		}
		
		locals.remove(locals.size()-1);
		stack.add(VerificationType.NULL);
		
		try {
			frame = frame.applyStackmapFull(locals, stack);
			Assert.fail("Expected exception!");
		} catch (StackmapFullStackOverflowException e) {
			//ignore
		}
		
		stack.remove(stack.size()-1);
		
		frame = frame.applyStackmapFull(locals, stack);
		
		locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.INT);
		locals.add(VerificationType.DOUBLE);
		locals.add(VerificationType.TOP);
		
		stack = new ArrayList<VerificationType>();
		stack.add(VerificationType.LONG);
		stack.add(VerificationType.INT);
		stack.add(VerificationType.DOUBLE);
		
		result = Frame.createFrame(locals, stack, 5);
		
		Assert.assertTrue(result.equals(frame));
		
		frame = Frame.createFrame(locals, new ArrayList<VerificationType>(), 1);
		try {
			frame.applyStackmapSameLocalsOneStackItem(VerificationType.DOUBLE);
			Assert.fail("Expected exception!");
		} catch (StackmapSameLocalsStackOverflowException e) {
			//ognore
		}
		
		frame =frame.applyStackmapSameLocalsOneStackItem(VerificationType.NULL);
		
		stack = new ArrayList<VerificationType>();
		stack.add(VerificationType.NULL);
		result = Frame.createFrame(locals, stack, 5);
		
		Assert.assertTrue(result.equals(frame));
		
		frame =frame.applyStackmapSame();
		
		stack = new ArrayList<VerificationType>();
		result = Frame.createFrame(locals, stack, 5);
		
		Assert.assertTrue(result.equals(frame));
		
		
		
		
	}
	
	
	@Override
	public boolean isInterface(String className) {
		Class cl = getClass(className);
		return cl.isInterface();
	}

	@Override
	public boolean isAssignable(String classTo, String classFrom) {
		if (classTo.equals(classFrom)) {
			return true;
		} else if (classTo.equals("java/lang/Object")) {
			return true;
		} else if (classFrom.equals("java/lang/Object")) {
			return classTo.equals("java/lang/Object");
		} else {
			Class fromClass = getClass(classFrom);
			return isAssignable(classTo, fromClass.getSuperclass().getName().replace('.', '/'));
		}
	}
	

	@Override
	public String merge(String classTo, String classFrom) {
		if (classTo.equals(classFrom)) {
			return classTo;
		} else if (classTo.equals("java/lang/Object") || classFrom.equals("java/lang/Object")) {
			return "java/lang/Object";
		} else {
			
			while (!isAssignable(classTo, classFrom)) {
				Class cl = getClass(classTo);
				classTo = cl.getSuperclass().getName().replace('.', '/');
			}
			return classTo;
		}
	}
	
	@Test
	public void applyStackMapTest2() {
		String descriptor = "(IJLjava/lang/Runnable;)V";
		Frame fr = Frame.createInitialFrame("java/lang/Dummy", false, true, 10, 5,new MethodDescriptor(descriptor), this);
		List<VerificationType> locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.INT);
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.TOP);
		locals.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		
		List<VerificationType> stack = new ArrayList<VerificationType>();
		
		Assert.assertTrue(fr.equals(Frame.createFrame(locals, stack, 5)));
		
		List<VerificationType> appendLocals = new ArrayList<VerificationType>();
		appendLocals.add(VerificationType.INT);
		appendLocals.add(VerificationType.LONG);
		appendLocals.add(VerificationType.FLOAT);
		fr = fr.applyStackmapAppend(appendLocals);
		locals.set(4, VerificationType.INT);
		locals.set(5, VerificationType.LONG);
		locals.set(6, VerificationType.TOP);
		locals.set(7, VerificationType.FLOAT);
		
		Assert.assertTrue(fr.equals(Frame.createFrame(locals, stack, 5)));
		
		fr = fr.applyStackmapChop(2);
		locals.set(5, VerificationType.TOP);
		locals.set(6, VerificationType.TOP);
		locals.set(7, VerificationType.TOP);
		
		Assert.assertTrue(fr.equals(Frame.createFrame(locals, stack, 5)));
		
	}
	
	@Test
	public void frameDifferenceTest() {
		
		List<VerificationType> locals = new ArrayList<VerificationType>();
		locals.add(VerificationType.INT);
		locals.add(VerificationType.LONG);
		locals.add(VerificationType.TOP);
		locals.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		locals.add(VerificationType.TOP);
		
		List<VerificationType> stack = new ArrayList<VerificationType>();
		
		Frame fr1 = Frame.createFrame(locals, stack, 5);
		Frame fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new SameFrame(), fr1.calculateFrameDifference(fr2));
		
		stack.add(new ObjectValueType("Ljava/lang/RuntimeException;", this));
		fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new SameLocalsOneStackItemFrame(new ObjectValueType("Ljava/lang/RuntimeException;", this)), fr1.calculateFrameDifference(fr2));
		
		stack = new ArrayList<VerificationType>();
		locals.set(3, VerificationType.TOP);
		fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new ChopFrame(1), fr1.calculateFrameDifference(fr2));
		
		List<VerificationType> append = new ArrayList<VerificationType>();
		append.add(VerificationType.DOUBLE);
		append.add(VerificationType.INT);
		
		locals.set(3, new ObjectValueType("Ljava/lang/Runnable;", this));
		locals.set(4, VerificationType.DOUBLE);
		locals.set(6, VerificationType.INT);
		fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new AppendFrame(append), fr1.calculateFrameDifference(fr2));
		
		List<VerificationType> locVars = new ArrayList<VerificationType>();
		locVars.add(VerificationType.FLOAT);
		locVars.add(VerificationType.LONG);
		locVars.add(new ObjectValueType("Ljava/lang/Runnable;", this));
		
		
		
		locals.set(0,VerificationType.FLOAT);
		locals.set(4, VerificationType.TOP);
		locals.set(6, VerificationType.TOP);
		fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new FullFrame(locVars, stack), fr1.calculateFrameDifference(fr2));
		
		stack.add(VerificationType.FLOAT);
		stack.add(VerificationType.INT);
		locals.set(4, VerificationType.DOUBLE);
		locals.set(6, VerificationType.INT);
		locVars.add(VerificationType.DOUBLE);
		locVars.add(VerificationType.INT);
		fr2 = Frame.createFrame(locals, stack, 5);
		Assert.assertEquals(new FullFrame(locVars, stack), fr1.calculateFrameDifference(fr2));
	}
	
	private Class getClass(String className) {
		String className2 = className.replace('/', '.');
		try {
			return Class.forName(className2);
		} catch (ClassNotFoundException e) {
			throw new UnknownClassException(-1, className2);
		}
	}

}
