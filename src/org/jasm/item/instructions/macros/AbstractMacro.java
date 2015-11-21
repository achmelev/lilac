package org.jasm.item.instructions.macros;

import java.util.List;

import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BipushInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LdcInstruction;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.LongLiteral;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;

public abstract class AbstractMacro implements IMacro {
	
	protected List<IMacroArgument> arguments;
	protected Instructions instructions;
	protected MacroCall call;

	@Override
	public void init(MacroCall call, Instructions instrs) {
		instructions = instrs;
		this.arguments = call.getArguments();
		this.call = call;
	}
	
	
	protected void emitError(SourceLocation location, String message) {
		instructions.emitErrorOnLocation((location != null)?location:call.getSourceLocation(), message);
	}
	
	protected AbstractInstruction createArgumentLessInstruction(short opCode) {
		if (!OpCodes.isArgumentLessInstruction(opCode)) {
			throw new IllegalArgumentException("Unknown argumentless opcode: "+Integer.toHexString(opCode));
		}
		return new ArgumentLessInstruction(opCode);
	}
	
	protected AbstractInstruction createBipushInstruction(byte value) {
		return new BipushInstruction(value);
	}
	
	protected AbstractInstruction createSipushInstruction(short value) {
		return new SipushInstruction(value);
	}
	
	protected AbstractInstruction createLdcInstruction(AbstractConstantPoolEntry cp) {
		return new LdcInstruction(cp);
	}
	
	protected ClassInfo getClassInfo(String className, SourceLocation location) {
		if (!IdentifierUtils.isValidJasmClassName(className)) {
			emitError(location, "malformed class or array name: "+className);
			return null;
		} else {
			return instructions.getConstantPool().getOrAddClassInfo(className);
		}
	}
	
	protected DoubleInfo getDoubleInfo(double value) {
		return instructions.getConstantPool().getOrAddDoubleInfo(value);
	}
	
	protected FloatInfo getFloatInfo(float value) {
		return instructions.getConstantPool().getOrAddFloatInfo(value);
	}
	
	protected IntegerInfo getIntegerInfo(int value) {
		return instructions.getConstantPool().getOrAddIntegerInfo(value);
	}
	
	protected LongInfo getLongInfo(long value) {
		return instructions.getConstantPool().getOrAddLongInfo(value);
	}
	
	protected StringInfo getStringInfo(String value) {
		return instructions.getConstantPool().getOrAddStringInfo(value);
	}
	
	protected IMacroArgument getArgument(int index) {
		return arguments.get(index);
	}
	
	protected int getNumberOfArguments() {
		return arguments.size();
	}
	
	protected boolean isString(IMacroArgument value) {
		return value instanceof StringLiteral;
	}
	
	protected boolean isInteger(IMacroArgument value) {
		return value instanceof LongLiteral;
	}
	
	protected boolean isFloatingPoint(IMacroArgument value) {
		return value instanceof DoubleLiteral;
	}
	
	protected boolean isSymbolReference(IMacroArgument value) {
		return value instanceof SymbolReference;
	}
	
	protected String getStringValue(IMacroArgument value) {
		return ((StringLiteral)value).getContent();
	}
	
	protected double getFloatingPointValue(IMacroArgument value) {
		return ((DoubleLiteral)value).getValue();
	}
	
	protected long getIntegerValue(IMacroArgument value) {
		return ((LongLiteral)value).getValue();
	}
	
	

}
