package org.jasm.item.instructions.macros.builtin;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.macros.AbstractMacro;
import org.jasm.item.instructions.macros.IMacroArgument;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class PrintMacro extends AbstractMacro {
	
	private ConcatMacro concat;
	private boolean hasStream;
	
	public PrintMacro(boolean hasStream){
		this.hasStream = hasStream;
	}

	@Override
	public List<AbstractInstruction> createInstructions() {
		List<AbstractInstruction> result = new ArrayList<AbstractInstruction>();
		if (hasStream) {
			IMacroArgument firstArgument = getArgument(0);
			TypeDescriptor t1 = getArgumentType(firstArgument);
			TypeDescriptor t2= new TypeDescriptor("Ljava/io/PrintStream;");
			pushArgument(firstArgument, result);
		} else {
			FieldrefInfo system_out = getFieldRefInfo("java/lang/System", "out", new TypeDescriptor("Ljava/io/PrintStream;"));
			result.add(createConstantPoolInstruction(OpCodes.getstatic, system_out));
		}
		result.addAll(concat.createInstructions());
		MethodrefInfo println = getMethodRefInfo("java/io/PrintStream", "println", new MethodDescriptor("(Ljava/lang/String;)V"));
		result.add(createConstantPoolInstruction(OpCodes.invokevirtual, println));
		
		return result;
		
	}

	@Override
	public boolean hasReturnValue() {
		return false;
	}

	@Override
	public TypeDescriptor getReturnType() {
		return null;
	}

	@Override
	protected boolean validateSpecialArgumentType(int index, IMacroArgument arg) {
		return false;
	}

	@Override
	protected boolean doResolve() {
		int startIndex = hasStream?1:0;
		if (getNumberOfArguments() < (startIndex+1)) {
			emitError(null, "wrong number of arguments");
		} else {
			if (hasStream) {
				IMacroArgument firstArgument = getArgument(0);
				TypeDescriptor t1 = getArgumentType(firstArgument);
				TypeDescriptor t2= new TypeDescriptor("Ljava/io/PrintStream;");
				if (!canCast(t1, t2)) {
					emitError(firstArgument.getSourceLocation(), "can not cast "+t1.getValue()+" to "+t2.getValue());
				}
			}
			concat = new ConcatMacro();
			concat.init_generated(getArguments().subList(startIndex, getArguments().size()), this);
			if (!concat.resolve()) {
				return false;
			}
		}
		return !hasError();
		
	}

}
