package org.jasm.test.verify;

import org.jasm.item.instructions.verify.error.UnknownClassException;
import org.jasm.item.instructions.verify.types.IClassQuery;
import org.jasm.item.instructions.verify.types.ObjectValueType;
import org.jasm.item.instructions.verify.types.UninitializedValueType;
import org.jasm.item.instructions.verify.types.VerificationType;
import org.jasm.type.descriptor.TypeDescriptor;
import org.junit.Assert;
import org.junit.Test;

public class VerificationTypeTest implements IClassQuery {
	
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
	
	private Class getClass(String className) {
		String className2 = className.replace('/', '.');
		try {
			return Class.forName(className2);
		} catch (ClassNotFoundException e) {
			throw new UnknownClassException(-1, className2);
		}
	}

}
