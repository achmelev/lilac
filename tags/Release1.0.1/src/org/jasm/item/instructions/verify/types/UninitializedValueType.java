package org.jasm.item.instructions.verify.types;

import org.jasm.type.descriptor.TypeDescriptor;

public class UninitializedValueType extends VerificationType {
	
	private TypeDescriptor desc;
	private int instructionIndex = -1;
	
	public UninitializedValueType(TypeDescriptor desc,int instructionIndex) {
		this.desc = desc;
		this.instructionIndex = instructionIndex;
		if (!desc.isObject()) {
			throw new IllegalArgumentException("only object types allowed: "+desc.getValue());
		}
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
	protected boolean isAssignableFromInt(IntType from) {
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
		return from.equals(this);
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
		if (from.equals(this)) {
			return this;
		} else {
			return TOP;
		}
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UninitializedValueType other = (UninitializedValueType) obj;
		if (instructionIndex != other.instructionIndex)
			return false;
		return true;
	}

	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public String toString() {
		return "uninitialized_value["+instructionIndex+","+desc.getValue()+"]";
	}

	public TypeDescriptor getDesc() {
		return desc;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}
	
	

}
