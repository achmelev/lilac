package org.jasm.item.instructions.macros;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.JasmConsts;
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
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.ShortLocalVariableInstruction;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.ClassReference;
import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.FieldReference;
import org.jasm.parser.literals.FloatLiteral;
import org.jasm.parser.literals.LongLiteral;
import org.jasm.parser.literals.MethodReference;
import org.jasm.parser.literals.NullLiteral;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.TypeDescriptor;

public abstract class AbstractMacro implements IMacro {
	
	private List<IMacroArgument> arguments;
	private	Map<IMacroArgument, TypeDescriptor> argumentTypes;
	private	Map<IMacroArgument, AbstractConstantPoolEntry> constantArguments;
	private	Map<IMacroArgument, LocalVariable> variableArguments;
	private Instructions instructions;
	private MacroCall call;
	
	private boolean hasError = false;

	@Override
	public void init(MacroCall call, Instructions instrs) {
		instructions = instrs;
		this.arguments = call.getArguments();
		this.call = call;
	}
	
	protected void emitError(SourceLocation location, String message) {
		instructions.emitErrorOnLocation((location != null)?location:call.getSourceLocation(), message);
		hasError = true;
	}
	
	protected AbstractInstruction createArgumentLessInstruction(short opCode) {
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
	
	protected AbstractInstruction createLocalVariableInstruction(short opCode, LocalVariable var) {
		LocalVariableInstruction instr = new LocalVariableInstruction(opCode, var);
		ShortLocalVariableInstruction shortVar = instr.createShortReplacement();
		if (shortVar == null) {
			return instr;
		} else {
			return shortVar;
		}
	}
	
	
	protected AbstractInstruction createLoadLocalVariableInstruction(LocalVariable var) {
		if (var.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			throw new IllegalArgumentException("return adress!");
		}
		return createLocalVariableInstruction(OpCodes.getOpcodeForName(var.getType()+"load"), var);
	}
	
	protected AbstractInstruction createStoreLocalVariableInstruction(LocalVariable var) {
		if (var.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			throw new IllegalArgumentException("return adress!");
		}
		return createLocalVariableInstruction(OpCodes.getOpcodeForName(var.getType()+"store"), var);
	}
	
	protected ClassInfo getClassInfo(String className) {
		return instructions.getConstantPool().getOrAddClassInfo(className);
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
	
	protected TypeDescriptor getArgumentType(IMacroArgument arg) {
		return argumentTypes.get(arg);
	}
	
	protected int getNumberOfArguments() {
		return arguments.size();
	}
	
	@Override
	public boolean resolve() {
		argumentTypes = new HashMap<IMacroArgument, TypeDescriptor>();
		constantArguments = new HashMap<IMacroArgument, AbstractConstantPoolEntry>();
		variableArguments = new HashMap<IMacroArgument, LocalVariable>();
		for (int i=0;i<arguments.size(); i++) {
			IMacroArgument arg = arguments.get(i);
			argumentTypes.put(arg, resolveArgumentType(arg));
			if (arg instanceof SymbolReference) {
				Object ref = resolveSymbolReference(arg, true);
				if (ref !=null) {
					if (ref instanceof AbstractConstantPoolEntry) {
						constantArguments.put(arg, (AbstractConstantPoolEntry)ref);
					} else {
						variableArguments.put(arg, (LocalVariable)ref);
					}
				}
			}
		}
		if (hasError) {
			return false;
		} else {
			return doResolve();
		}
	}
	
	protected boolean isLiteral(IMacroArgument arg) {
		return (arg instanceof DoubleLiteral) ||
				(arg instanceof LongLiteral) ||
				(arg instanceof StringLiteral) ||
				(arg instanceof NullLiteral);
				
	}
	
	protected boolean isReference(IMacroArgument arg) {
		return (arg instanceof SymbolReference) ||
				(arg instanceof FieldReference) ||
				(arg instanceof MethodReference) ||
				(arg instanceof ClassReference);
				
	}
	
	protected boolean isClassReference(IMacroArgument arg) {
		return (arg instanceof ClassReference);			
	}
	
	protected boolean isFieldReference(IMacroArgument arg) {
		return (arg instanceof FieldReference);			
	}
	
	protected boolean isMethodReference(IMacroArgument arg) {
		return (arg instanceof MethodReference);			
	}
	
	protected boolean isSymbolReference(IMacroArgument arg) {
		return arg instanceof SymbolReference;
	}
	
	protected boolean isConstantReference(IMacroArgument arg) {
		return constantArguments.containsKey(arg);		
	}
	
	protected boolean isVariableReference(IMacroArgument arg) {
		return variableArguments.containsKey(arg);
	}
	
	private Object resolveSymbolReference(IMacroArgument arg, boolean emitError) {
		Object result = null;
		SymbolReference sym = (SymbolReference)arg;
		LocalVariable var = instructions.getVariablesPool().getByName(sym.getSymbolName());
		if (var != null) {
			result =  var;
		} else {
			result =  instructions.getConstantPool().getSymbolTable().get(sym.getSymbolName());
		}
		
		if (result == null && emitError) {
			if (emitError) {
				emitError(arg.getSourceLocation(), "unknown name");
			} else {
				throw new RuntimeException("unknown name: "+sym.getSymbolName());
			}
			
		} 
		
		return result;
	}
	
	private TypeDescriptor resolveArgumentType(IMacroArgument arg) {
		if (isLiteral(arg)) {
			return getLiteralType(arg);
		} else if (isSymbolReference(arg)) {
			Object ref = resolveSymbolReference(arg, true);
			if (ref != null) {
				if (ref instanceof LocalVariable) {
					return getVariableType((LocalVariable)ref);
				} else {
					return getConstantType((AbstractConstantPoolEntry)ref);
				}
			} else {
				return null;
			}
		} else if (arg instanceof MacroCall) {
			return ((MacroCall)arg).getMacro().getReturnType();
		} else {
			return null;
		}
	}
	
	private TypeDescriptor getLiteralType(IMacroArgument arg) {
		if (arg instanceof LongLiteral)  {
			LongLiteral lit = (LongLiteral)arg;
			long value = lit.getValue();
			if (value>Integer.MAX_VALUE || value<Integer.MIN_VALUE) {
				return new TypeDescriptor("J");
			} else {
				return new TypeDescriptor("I");
			}
		} else if (arg instanceof DoubleLiteral) {
			DoubleLiteral lit = (DoubleLiteral)arg;
			FloatLiteral lit2 = new FloatLiteral(0,0,lit.getContent());
			if (lit2.isValid()) {
				return new TypeDescriptor("F");
			} else {
				return new TypeDescriptor("D");
			}
		} else if (arg instanceof StringLiteral) {
			return new TypeDescriptor("Ljava/lang/String;");
		} else if (arg instanceof NullLiteral) {
			return new TypeDescriptor("Ljava/lang/Object;");
		} else {
			throw new IllegalArgumentException("Unknown literal type: "+arg.getClass().getName());
		}
	}
	
	private TypeDescriptor getVariableType(LocalVariable arg) {
		if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_DOUBLE) {
			return new TypeDescriptor("D");
		} else if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_FLOAT) {
			return new TypeDescriptor("F");
		} else if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_INT) {
			return new TypeDescriptor("I");
		} else if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_LONG) {
			return new TypeDescriptor("J");
		} else if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE) {
			return new TypeDescriptor("Ljava/lang/Object;");
		} else if (arg.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			return null;
		} else {
			throw new IllegalArgumentException("Unknown var type: "+arg.getType());
		}
	}
	
	private TypeDescriptor getConstantType(AbstractConstantPoolEntry arg) {
		if (arg instanceof DoubleInfo) {
			return new TypeDescriptor("D");
		} else if (arg instanceof FloatInfo) {
			return new TypeDescriptor("F");
		} else if (arg instanceof IntegerInfo) {
			return new TypeDescriptor("I");
		} else if (arg instanceof LongInfo) {
			return new TypeDescriptor("J");
		} else if (arg instanceof StringInfo) {
			return new TypeDescriptor("Ljava/lang/String;");
		} else {
			return null;
		}
	}
	
	protected void pushIntArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		long value = ((LongLiteral)arg).getValue();
		if (value<=Byte.MAX_VALUE && value<=Byte.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else if (value<=Short.MAX_VALUE && value<=Short.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else {
			IntegerInfo ii = getIntegerInfo((int)value);
			result.add(createLdcInstruction(ii));
		}
	}
	
	protected void pushLongArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		long value = ((LongLiteral)arg).getValue();
		LongInfo ii = getLongInfo(value);
		result.add(createLdcInstruction(ii));
	}
	
	protected void pushDoubleArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		double value = ((DoubleLiteral)arg).getValue();
		DoubleInfo ii = getDoubleInfo(value);
		result.add(createLdcInstruction(ii));
	}
	
	protected void pushFloatArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		FloatLiteral f = new FloatLiteral(0, 0, ((DoubleLiteral)arg).getContent());
		float value = f.getValue();
		FloatInfo ii = getFloatInfo(value);
		result.add(createLdcInstruction(ii));
	}
	
	protected void pushStringArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		String value = ((StringLiteral)arg).getContent();
		StringInfo ii = getStringInfo(value);
		result.add(createLdcInstruction(ii));
	}
	
	protected AbstractConstantPoolEntry getConstantArgumentValue(IMacroArgument arg) {
		return constantArguments.get(arg);
	}
	
	protected LocalVariable getVariableArgumentValue(IMacroArgument arg) {
		return variableArguments.get(arg);
	}
	
	protected void pushConstantArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		AbstractConstantPoolEntry cp = (AbstractConstantPoolEntry)constantArguments.get(arg);
		result.add(createLdcInstruction(cp));
	}
	
	protected void pushVariableArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		LocalVariable var = variableArguments.get(arg);
		if (var.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			throw new IllegalArgumentException("return adress push!");
		}
		result.add(createLoadLocalVariableInstruction(var));
	}
	
	protected void pushMacroCallArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!((MacroCall)arg).getMacro().hasReturnValue()) {
			throw new IllegalArgumentException("the macro has no return value");
		}
		List<AbstractInstruction> instrs = ((MacroCall)arg).getMacro().createInstructions();
		result.addAll(instrs);
	}
	
	protected void pushNullArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!(arg instanceof NullLiteral)) {
			throw new IllegalArgumentException(arg.getClass().getName());
		}
		result.add(createArgumentLessInstruction(OpCodes.aconst_null));
	}
	
	protected void pushArgument(IMacroArgument arg,   List<AbstractInstruction> result) {
		TypeDescriptor type = argumentTypes.get(arg);
		if (type.isFloat() && isLiteral(arg)) {
			pushFloatArgument(arg, result);
		} else if (type.isDouble() && isLiteral(arg)) {
			pushDoubleArgument(arg, result);
		} else if (type.isInteger() && isLiteral(arg)) {
			pushIntArgument(arg, result);
		} else if (type.isLong() && isLiteral(arg)) {
			pushLongArgument(arg, result);
		} else if (type.isObject() && type.getClassName().equals("java/lang/String") && isLiteral(arg)) {
			pushStringArgument(arg, result);
		} else if (arg instanceof NullLiteral) {
			pushNullArgument(arg, result);
		} else if (isConstantReference(arg)) {
			pushConstantArgument(arg, result);
		} else if (isVariableReference(arg)) {
			pushVariableArgument(arg, result);
		} else if (arg instanceof MacroCall) {
			pushMacroCallArgument(arg, result);
		} else {
			throw new IllegalArgumentException("Unknown combination: "+arg+"; "+type);
		}
	}
	
	protected abstract boolean doResolve();

	public boolean hasError() {
		return hasError;
	}
	
	
	

}
