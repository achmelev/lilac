package org.jasm.item.instructions.verify.types;


public class TopType extends VerificationType {
	
	TopType() {
		
	}

	@Override
	protected boolean isAssignableFromDouble(DoubleType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromFloat(FloatType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromInt(IntType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromLong(LongType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromNull(NullType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromObjectValue(ObjectValueType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromTop(TopType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromUninitializedThis(
			UninitializedThisType from) {
		return true;
	}

	@Override
	protected boolean isAssignableFromUninitializedValue(
			UninitializedValueType from) {
		return true;
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
	protected VerificationType mergeWithInt(IntType from) {
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
	public int getSize() {
		return 1;
	}
	
	@Override
	public String toString() {
		return "top";
	}

}
