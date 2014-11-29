package org.jasm.item.attribute;


import org.jasm.JasmConsts;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.IAttributesContainer;
import org.jasm.item.clazz.Method;

public abstract class AbstractAnnotationTargetType extends AbstractByteCodeItem {
	
	protected short targetType = -1;
	
	public AbstractAnnotationTargetType() {
		
	}
	
	public AbstractAnnotationTargetType(short targetType) {
		this.targetType = targetType;
		if (targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER ||
			targetType == JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER ||
			(targetType>=JasmConsts.ANNOTATION_TARGET_SUPERTYPE && targetType<=JasmConsts.ANNOTATION_TARGET_THROWS) ||
			(targetType>=JasmConsts.ANNOTATION_TARGET_LOCAL_VAR && targetType<=JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF)) {
			//OK
		} else {
			throw new IllegalArgumentException("Unknown target type: "+Integer.toHexString(targetType));
		}
	}
	
	

	public short getTargetType() {
		return targetType;
	}

	public void setTargetType(short targetType) {
		this.targetType = targetType;
	}
	
	public boolean isInClass() {
		return !isInCode() && !isInField() && !isInMethod() ;
	}
	
	public boolean isInCode() {
		return isAncestor(CodeAttributeContent.class);
	}
	
	public boolean isInMethod() {
		return !isInCode() && isAncestor(Method.class);
	}
	
	public boolean isInField() {
		return isAncestor(Field.class);
	}
	
	private <T extends AbstractByteCodeItem & IAttributesContainer> boolean isAncestor(Class<T> clazz) {
		return getAncestor(clazz) != null;
	}
	
	protected void emitIllegalInContextError() {
		emitError(null, "target type illegal in this context");
	}
	

}
