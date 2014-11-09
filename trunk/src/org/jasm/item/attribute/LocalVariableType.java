package org.jasm.item.attribute;

import org.jasm.JasmConsts;
import org.jasm.item.descriptor.IllegalDescriptorException;


public class LocalVariableType extends DebugLocalVariable {

	@Override
	protected char getVariableType() throws IllegalDescriptorException {
		return JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE;
	}
	
	

}
