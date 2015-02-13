package org.jasm.item.instructions.verify.types;

import org.jasm.type.descriptor.TypeDescriptor;

public class ObjectValueType extends VerificationType {
	
	
	private TypeDescriptor desc;
	private IClassQuery query;
	
	public ObjectValueType(String desc, IClassQuery query) {
		this(new TypeDescriptor(desc),query);
	}
	
	public ObjectValueType(TypeDescriptor desc, IClassQuery query) {
		this.desc = desc;
		if (!(desc.isObject() || desc.isArray())) {
			throw new IllegalArgumentException("only object and array types allowed: "+desc.getValue());
		}
		this.query = query;
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
		if (this.equals(from)) {
			return true;
		}
		if (this.equals(VerificationType.OBJECT)) {
			return true;
		} else if (desc.isObject() && query.isInterface(desc.getClassName())) {
			return true;
		} else {
			if (from.getDesc().isObject() && query.isInterface(from.getDesc().getClassName())) {
				return false;
			}
			
			if (this.getDesc().isArray()) {
				if (from.getDesc().isArray()) {
					if (this.getDesc().getComponentType().isPrimitive()) {
						return from.getDesc().equals(this.getDesc());
					} else {
						if (from.getDesc().getComponentType().isPrimitive()) {
							return false;
						} else {
							ObjectValueType newThis = new ObjectValueType(this.getDesc().getComponentType(), query);
							ObjectValueType newFrom = new ObjectValueType(from.getDesc().getComponentType(), query);
							return newThis.isAssignableFrom(newFrom);
						}
					}
				} else {
					return false;
				}
			} else {
				if (from.getDesc().isArray()) {
					return false;
				} else {
					return query.isAssignable(this.getDesc().getClassName(), from.getDesc().getClassName());
				}
			}
		}
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
		if (this.equals(VerificationType.OBJECT)) {
			return this;
		}
		if (from.equals(VerificationType.OBJECT)) {
			return from;
		}
		
		if (this.getDesc().isObject() && query.isInterface(this.getDesc().getClassName())) {
			return VerificationType.OBJECT.cloneWithQuery(query);
		}
		
		if (from.getDesc().isObject() && query.isInterface(from.getDesc().getClassName())) {
			return VerificationType.OBJECT.cloneWithQuery(query);
		}
		
		if (this.getDesc().isArray()) {
			if (from.getDesc().isArray()) {
				TypeDescriptor thisComponent = this.getDesc().getComponentType();
				TypeDescriptor fromComponent = from.getDesc().getComponentType();
				if (thisComponent.isPrimitive() || fromComponent.isPrimitive()) {
					if (thisComponent.equals(fromComponent)) {
						return this;
					} else {
						return VerificationType.OBJECT.cloneWithQuery(query);
					}
				} else {
					ObjectValueType componentMerge = (ObjectValueType)new ObjectValueType(thisComponent, query).mergeWith(new ObjectValueType(fromComponent, query));
					return new ObjectValueType(new TypeDescriptor("["+componentMerge.getDesc().getValue()), query);
				}
			} else {
				return VerificationType.OBJECT.cloneWithQuery(query);
			}
		} else {
			if (from.getDesc().isArray()) {
				return VerificationType.OBJECT.cloneWithQuery(query);
			} else {
				String rootClass = query.merge(this.getDesc().getClassName(), from.getDesc().getClassName());
				return new ObjectValueType(new TypeDescriptor("L"+rootClass+";"), query);
			}
		}
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

	public TypeDescriptor getDesc() {
		return desc;
	}

	public void setQuery(IClassQuery query) {
		this.query = query;
	}

	public ObjectValueType cloneWithQuery(IClassQuery query) {
		return new ObjectValueType(desc, query);
	}

	

	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public String toString() {
		return desc.toString();
	}
	
	
	
	

}
