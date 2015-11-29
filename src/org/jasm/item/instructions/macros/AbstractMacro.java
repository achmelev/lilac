package org.jasm.item.instructions.macros;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.JasmConsts;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BipushInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
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
import org.jasm.type.descriptor.MethodDescriptor;
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
	
	protected AbstractInstruction createConstantPoolInstruction(short opCode, AbstractConstantPoolEntry constant) {
		return new ConstantPoolInstruction(opCode, constant);
	}
	
	protected ClassInfo getClassInfo(String className) {
		return instructions.getConstantPool().getOrAddClassInfo(className);
	}
	
	protected FieldrefInfo getFieldRefInfo(String className, String name, TypeDescriptor type) {
		return instructions.getConstantPool().getOrAddFieldrefInfo(className, name, type.getValue());
	}
	
	protected MethodrefInfo getMethodRefInfo(String className, String name, MethodDescriptor type) {
		return instructions.getConstantPool().getOrAddMethofdrefInfo(className, className, type.getValue());
	}
	
	protected InterfaceMethodrefInfo getInterfaceMethodRefInfo(String className, String name, MethodDescriptor type) {
		return instructions.getConstantPool().getOrAddInterfaceMethofdrefInfo(className, className, type.getValue());
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
			} else if (arg instanceof FieldReference) {
				FieldReference ref = (FieldReference)arg;
				String className = resolveClassName(ref.getClassName());
				if (className != null) {
					ref.setClassName(className);
				} else {
					hasError = true;
				}
			
			} else if (arg instanceof MethodReference) {
				MethodReference ref = (MethodReference)arg;
				String className = resolveClassName(ref.getClassName());
				if (className != null) {
					ref.setClassName(className);
				} else {
					hasError = true;
				}
			}
		}
		if (hasError) {
			return false;
		} else {
			return doResolve();
		}
	}
	
	private String resolveClassName(String className) {
		if (!className.equals("")) {
			return className;
		} else {
			if (instructions.getAncestor(Clazz.class).getThisClass() !=null) {
				return instructions.getAncestor(Clazz.class).getThisClass().getClassName();
			} else {
				return null;
			}
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
	
	protected boolean isArrayType(IMacroArgument arg) {
		return arg instanceof SymbolReference;
	}
	
	protected boolean isConstantSymbolReference(IMacroArgument arg) {
		return constantArguments.containsKey(arg);		
	}
	
	protected boolean isPushableConstantSymbolReference(IMacroArgument arg) {
		return constantArguments.containsKey(arg)
				&& isPushableConstant(constantArguments.get(arg));
	}
	
	protected boolean isPushableConstant(AbstractConstantPoolEntry entry) {
		return ((entry instanceof DoubleInfo) ||
				(entry instanceof FloatInfo) ||
				(entry instanceof IntegerInfo) ||
				(entry instanceof LongInfo) ||
				(entry instanceof StringInfo) ||
				(entry instanceof ClassInfo) ||
				(entry instanceof MethodTypeInfo) ||
				(entry instanceof MethodHandleInfo));
	}
	
	protected boolean isVariableSymbolReference(IMacroArgument arg) {
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
		} else if (arg instanceof ClassReference) {
			return new TypeDescriptor("Ljava/lang/Class;");	
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
		} else if (arg instanceof ClassInfo) {
			return new TypeDescriptor("Ljava/lang/Class;");
		} else if (arg instanceof MethodHandleInfo) {
			return new TypeDescriptor("Ljava/lang/invoke/MethodHandle;");
		} else if (arg instanceof MethodTypeInfo) {
			return new TypeDescriptor("Ljava/lang/invoke/Method;");
		} else {
			return null;
		}
	}
	
	protected List<AbstractInstruction> pushIntArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		long value = ((LongLiteral)arg).getValue();
		return pushIntValue((int)value, result);
	}
	
	protected List<AbstractInstruction>  pushIntValue(int value, List<AbstractInstruction> result) {
		if (value<=Byte.MAX_VALUE && value<=Byte.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else if (value<=Short.MAX_VALUE && value<=Short.MIN_VALUE) {
			result.add(createBipushInstruction((byte)value));
		} else {
			IntegerInfo ii = getIntegerInfo((int)value);
			result.add(createLdcInstruction(ii));
		}
		return result;
	}
	
	protected List<AbstractInstruction> pushLongArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		long value = ((LongLiteral)arg).getValue();
		return pushLongValue(value, result);
	}
	
	protected List<AbstractInstruction> pushLongValue(long value, List<AbstractInstruction> result) {
		LongInfo ii = getLongInfo(value);
		result.add(createLdcInstruction(ii));
		return result;
	}
	
	protected List<AbstractInstruction> pushDoubleArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		double value = ((DoubleLiteral)arg).getValue();
		return pushDoubleValue(value, result);
	}
	
	protected List<AbstractInstruction> pushDoubleValue(double value, List<AbstractInstruction> result) {
		DoubleInfo ii = getDoubleInfo(value);
		result.add(createLdcInstruction(ii));
		return result;
	}
	
	protected List<AbstractInstruction> pushFloatArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		FloatLiteral f = new FloatLiteral(0, 0, ((DoubleLiteral)arg).getContent());
		float value = f.getValue();
		return pushFloatValue(value, result);
	}
	
	protected List<AbstractInstruction> pushFloatValue(float value, List<AbstractInstruction> result) {
		FloatInfo ii = getFloatInfo(value);
		result.add(createLdcInstruction(ii));
		return result;
	}
	
	protected List<AbstractInstruction> pushStringArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		String value = ((StringLiteral)arg).getContent();
		pushStringValue(value, result);
		return result;
	}
	
	protected List<AbstractInstruction> pushStringValue(String value, List<AbstractInstruction> result) {
		if (value == null) {
			throw new IllegalArgumentException(value);
		}
		StringInfo ii = getStringInfo(value);
		result.add(createLdcInstruction(ii));
		return result;
	}
	
	protected AbstractConstantPoolEntry getConstantSymbolReferenceValue(IMacroArgument arg) {
		if (!constantArguments.containsKey(arg)) {
			throw new IllegalArgumentException("unknown constant"+arg);
		}
		return constantArguments.get(arg);
	}
	
	protected LocalVariable getVariableSymbolReferenceValue(IMacroArgument arg) {
		if (!variableArguments.containsKey(arg)) {
			throw new IllegalArgumentException("unknown constant"+arg);
		}
		return variableArguments.get(arg);
	}
	
	protected List<AbstractInstruction> pushConstantSymbolReferenceArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!isPushableConstantSymbolReference(arg)) {
			throw new IllegalArgumentException("isn't pushable: "+arg);
		}
		return pushConstantValue(constantArguments.get(arg), result);
	}
	
	protected List<AbstractInstruction> pushConstantValue(AbstractConstantPoolEntry entry, List<AbstractInstruction> result) {
		if (!isPushableConstant(entry)) {
			throw new IllegalArgumentException("isn't pushable: "+entry);
		}
		result.add(createLdcInstruction(entry));
		return result;
	}
	
	protected List<AbstractInstruction> pushVariableSymbolReferenceArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!isVariableSymbolReference(arg)) {
			throw new IllegalArgumentException(""+arg);
		}
		LocalVariable var = variableArguments.get(arg);
		return pushVariable(var, result);
	}
	
	protected List<AbstractInstruction> pushVariable(LocalVariable var, List<AbstractInstruction> result) {
		if (var.getType() == JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS) {
			throw new IllegalArgumentException("return adress push!");
		}
		result.add(createLoadLocalVariableInstruction(var));
		return result;
	}
	
	protected void pushMacroCallArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!((MacroCall)arg).getMacro().hasReturnValue()) {
			throw new IllegalArgumentException("the macro has no return value");
		}
		List<AbstractInstruction> instrs = ((MacroCall)arg).getMacro().createInstructions();
		result.addAll(instrs);
	}
	
	protected List<AbstractInstruction> pushNullArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		if (!(arg instanceof NullLiteral)) {
			throw new IllegalArgumentException(arg.getClass().getName());
		}
		return pushNullValue(result);
	}
	
	protected List<AbstractInstruction> pushNullValue(List<AbstractInstruction> result) {
		result.add(createArgumentLessInstruction(OpCodes.aconst_null));
		return result;
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
		} else if (isConstantSymbolReference(arg)) {
			pushConstantSymbolReferenceArgument(arg, result);
		} else if (isVariableSymbolReference(arg)) {
			pushVariableSymbolReferenceArgument(arg, result);
		} else if (arg instanceof MacroCall) {
			pushMacroCallArgument(arg, result);
		} else if (arg instanceof ClassReference) {
			ClassInfo value = getClassInfo(((ClassInfo)arg).getClassName());
			pushConstantValue(value, result);
		} else {
			throw new IllegalArgumentException("Unknown combination: "+arg+"; "+type);
		}
	}
	
	protected void pushValueFromField(IMacroArgument object, IMacroArgument field, List<AbstractInstruction> result) {
		
		if (object != null) {
			pushArgument(object, result);
		}
		short opCode = (object == null)?OpCodes.getstatic:OpCodes.getfield;
		if (isConstantSymbolReference(field)) {
			result.add(createConstantPoolInstruction(opCode, constantArguments.get(field)));
		} else if (isFieldReference(field)) {
			FieldReference ref = (FieldReference)field;
			result.add(createConstantPoolInstruction(opCode, getFieldRefInfo(ref.getClassName(), ref.getFieldName(), ref.getDescriptor())));
		} else {
			throw new IllegalArgumentException("field: "+field);
		}
	}
	
	protected abstract boolean doResolve();

	public boolean hasError() {
		return hasError;
	}
	
	
	

}
