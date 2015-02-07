package org.jasm.item.instructions.verify.types;

import org.jasm.type.descriptor.TypeDescriptor;


public abstract class VerificationType {
	
	public static final VerificationType TOP = new TopType();
	public static final VerificationType DOUBLE = new DoubleType();
	public static final VerificationType FLOAT = new FloatType();
	public static final VerificationType INT = new IntType();
	public static final VerificationType LONG = new LongType();
	public static final VerificationType NULL = new NullType();
	public static final VerificationType UNINITIALIZED_THIS = new UninitializedThisType();
	public static final ObjectValueType OBJECT = new ObjectValueType(new TypeDescriptor("Ljava/lang/Object;"), null);
	
	
	public boolean isAssignableFrom(VerificationType from) {
		if (from instanceof DoubleType) {
			return isAssignableFromDouble((DoubleType)from);
		} else if (from instanceof FloatType) {
			return isAssignableFromFloat((FloatType)from);
		} else if (from instanceof IntType) {
			return isAssignableFromInt((IntType)from);
		} else if (from instanceof LongType) {
			return isAssignableFromLong((LongType)from);
		} else if (from instanceof NullType) {
			return isAssignableFromNull((NullType)from);
		} else if (from instanceof ObjectValueType) {
			return isAssignableFromObjectValue((ObjectValueType)from);
		} else if (from instanceof TopType) {
			return isAssignableFromTop((TopType)from);
		} else if (from instanceof UninitializedThisType) {
			return isAssignableFromUninitializedThis((UninitializedThisType)from);
		} else if (from instanceof UninitializedValueType) {
			return isAssignableFromUninitializedValue((UninitializedValueType)from);
		} else {
			throw new IllegalStateException("Unknown Type: "+from);
		}
	}
	
	public VerificationType mergeWith(VerificationType from) {
		if (from instanceof DoubleType) {
			return mergeWithDouble((DoubleType)from);
		} else if (from instanceof FloatType) {
			return  mergeWithFloat((FloatType)from);
		} else if (from instanceof IntType) {
			return  mergeWithInt((IntType)from);
		} else if (from instanceof LongType) {
			return  mergeWithLong((LongType)from);
		} else if (from instanceof NullType) {
			return  mergeWithNull((NullType)from);
		} else if (from instanceof ObjectValueType) {
			return  mergeWithObjectValue((ObjectValueType)from);
		} else if (from instanceof TopType) {
			return  mergeWithTop((TopType)from);
		} else if (from instanceof UninitializedThisType) {
			return  mergeWithUninitializedThis((UninitializedThisType)from);
		} else if (from instanceof UninitializedValueType) {
			return  mergeWithUninitializedValue((UninitializedValueType)from);
		} else {
			throw new IllegalStateException("Unknown Type: "+from);
		}
	}
	
	
	protected abstract boolean isAssignableFromDouble(DoubleType from);
	protected abstract boolean isAssignableFromFloat(FloatType from);
	protected abstract boolean isAssignableFromInt(IntType from);
	protected abstract boolean isAssignableFromLong(LongType from);
	protected abstract boolean isAssignableFromNull(NullType from);
	protected abstract boolean isAssignableFromObjectValue(ObjectValueType from);
	protected abstract boolean isAssignableFromTop(TopType from);
	protected abstract boolean isAssignableFromUninitializedThis(UninitializedThisType from);
	protected abstract boolean isAssignableFromUninitializedValue(UninitializedValueType from);
	
	protected abstract VerificationType mergeWithDouble(DoubleType from);
	protected abstract VerificationType mergeWithFloat(FloatType from);
	protected abstract VerificationType mergeWithInt(IntType from);
	protected abstract VerificationType mergeWithLong(LongType from);
	protected abstract VerificationType mergeWithNull(NullType from);
	protected abstract VerificationType mergeWithObjectValue(ObjectValueType from);
	protected abstract VerificationType mergeWithTop(TopType from);
	protected abstract VerificationType mergeWithUninitializedThis(UninitializedThisType from);
	protected abstract VerificationType mergeWithUninitializedValue(UninitializedValueType from);
	

}
