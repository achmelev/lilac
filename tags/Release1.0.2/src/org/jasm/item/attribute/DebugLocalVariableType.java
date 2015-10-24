package org.jasm.item.attribute;

import org.jasm.JasmConsts;
import org.jasm.item.instructions.Instructions;
import org.jasm.type.descriptor.IllegalDescriptorException;


public class DebugLocalVariableType extends DebugLocalVariable {

	@Override
	protected char getVariableType() throws IllegalDescriptorException {
		return JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE;
	}

	@Override
	protected void checkDescriptor() {
		Instructions instr = ((CodeAttributeContent)getParent().getParent().getParent().getParent()).getInstructions();
		//TODO - Parsing signatures
		variable = instr.getVariablesPool().checkAndLoad(this, variableReference, getVariableType(), false);
		if (variable != null) {
			index = variable.getIndex();
		}
	}
	
	

}
