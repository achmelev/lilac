package org.jasm.item.attribute;


import org.jasm.JasmConsts;
import org.jasm.item.AbstractByteCodeItem;

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
	

}
