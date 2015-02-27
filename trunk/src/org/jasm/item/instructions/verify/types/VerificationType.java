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
	public static final VerificationType UNINITIALIZED = new Uninitialized();
	public static final ObjectValueType  OBJECT = new ObjectValueType(new TypeDescriptor("Ljava/lang/Object;"), null);
	public static final ObjectValueType  THROWABLE = new ObjectValueType(new TypeDescriptor("Ljava/lang/Throwable;"), null);
	public static final ObjectValueType  STRING = new ObjectValueType(new TypeDescriptor("Ljava/lang/String;"), null);
	public static final ObjectValueType  METHOD_TYPE = new ObjectValueType(new TypeDescriptor("Ljava/lang/invoke/MethodType;"), null);
	public static final ObjectValueType  METHOD_HANDLE = new ObjectValueType(new TypeDescriptor("Ljava/lang/invoke/MethodHandle;"), null);
	public static final ObjectValueType  CLASS = new ObjectValueType(new TypeDescriptor("Ljava/lang/Class;"), null);
	public static final ReferenceType REFERENCE = new ReferenceType();
	public static final OneWordType ONE_WORD = new OneWordType();
	public static final TwoWordType TWO_WORD = new TwoWordType();
	
	
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
	
	public abstract int getSize();
	
	public static VerificationType createTypeFromDescriptor(TypeDescriptor desc, IClassQuery query) {
		if (desc.isArray()) {
			return new ObjectValueType(desc, query);
		} else if (desc.isBoolean()) {
			return VerificationType.INT;
		} else if (desc.isCharacter()) {
			return VerificationType.INT;
		} else if (desc.isByte()) {
			return VerificationType.INT;
		} else if (desc.isDouble()) {
			return VerificationType.DOUBLE;
		} else if (desc.isFloat()) {
			return VerificationType.FLOAT;
		} else if (desc.isInteger()) {
			return VerificationType.INT;
		} else if (desc.isLong()) {
			return VerificationType.LONG;
		} else if (desc.isObject()) {
			return new ObjectValueType(desc, query);
		} else if (desc.isShort()) {
			return VerificationType.INT;
		} else {
			throw new IllegalStateException("Unknowhn descriptor type: "+desc);
		}
	}
	
}
