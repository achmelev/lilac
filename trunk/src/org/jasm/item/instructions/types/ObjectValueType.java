package org.jasm.item.instructions.types;

import org.jasm.type.descriptor.TypeDescriptor;

public class ObjectValueType extends VerificationType {
	
	private TypeDescriptor desc;
	
	public ObjectValueType(TypeDescriptor desc) {
		this.desc = desc;
		if (!(desc.isObject() || desc.isArray())) {
			throw new IllegalArgumentException("only object and array types allowed: "+desc.getValue());
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
		return true;
	}

	@Override
	protected boolean isAssignableFromObjectValue(ObjectValueType from) {
		// TODO 
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
	protected VerificationType mergeWithInt(IntType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithLong(LongType from) {
		return TOP;
	}

	@Override
	protected VerificationType mergeWithNull(NullType from) {
		return this;
	}

	@Override
	protected VerificationType mergeWithObjectValue(ObjectValueType from) {
		// TODO
		return null;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectValueType other = (ObjectValueType) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		return true;
	}
	
	

}
