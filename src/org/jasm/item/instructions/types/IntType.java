package org.jasm.item.instructions.types;

public class IntType extends VerificationType {
	
	public IntType() {
		
	}

	@Override
	protected boolean isAssignableFromDouble(DoubleType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromFloat(FloatType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromLong(LongType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromNull(NullType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromObjectValue(ObjectValueType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromTop(TopType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromUninitializedThis(
			UninitializedThisType from) {
		return false;
	}

	@Override
	protected boolean isAssignableFromUninitializedValue(
			UninitializedValueType from) {
		return false;
	}

	@Override
	protected VerificationType mergeWithDouble(DoubleType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithFloat(FloatType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithLong(LongType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithNull(NullType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithObjectValue(ObjectValueType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithTop(TopType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithUninitializedThis(
			UninitializedThisType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithUninitializedValue(
			UninitializedValueType from) {
		return TOP;
	}

	@Override
	protected boolean isAssignableFromInt(IntType from) {
		return true;
	}

	@Override
	protected VerificationType mergeWithInt(IntType from) {
		return this;
	}

}
