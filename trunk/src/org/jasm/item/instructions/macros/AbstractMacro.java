package org.jasm.item.instructions.macros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.JasmConsts;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.Method;
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
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LdcInstruction;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.ShortLocalVariableInstruction;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.FloatLiteral;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.LongLiteral;
import org.jasm.parser.literals.NullLiteral;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;


public abstract class AbstractMacro implements IMacro {
	
	private List<IMacroArgument> arguments;
	private	Map<IMacroArgument, TypeDescriptor> argumentTypes;
	private	Map<IMacroArgument, TypeDescriptor> castTypes;
	private	Map<IMacroArgument, AbstractConstantPoolEntry> constantArguments;
	private	Map<IMacroArgument, LocalVariable> variableArguments;
	private	Map<IMacroArgument, AbstractInstruction> instructionReferences;
	private	Map<IMacroArgument, Field> localFieldReferences;
	private	Map<IMacroArgument, Method> localMethodReferences;
	
	//Cast Maps
	
	private static Map<String, short[]> primitiveCastInstructions;
	private static Map<String, String> primitiveToBox;
	private static Map<String, String> boxToPrimitive;

	private Instructions instructions;
	private MacroCall call;
	
	private boolean hasError = false;
	
	static {
		//B,C,D,F,I,J,S,Z
		primitiveCastInstructions = new HashMap<String, short[]>();
		primitiveCastInstructions.put("B2C", new short[]{OpCodes.i2c});
		primitiveCastInstructions.put("B2D", new short[]{OpCodes.i2d});
		primitiveCastInstructions.put("B2F", new short[]{OpCodes.i2f});
		primitiveCastInstructions.put("B2J", new short[]{OpCodes.i2l});
		primitiveCastInstructions.put("B2S", new short[]{OpCodes.i2s});
		primitiveCastInstructions.put("C2B", new short[]{OpCodes.i2b});
		primitiveCastInstructions.put("C2D", new short[]{OpCodes.i2d});
		primitiveCastInstructions.put("C2F", new short[]{OpCodes.i2f});
		primitiveCastInstructions.put("C2J", new short[]{OpCodes.i2l});
		primitiveCastInstructions.put("C2J", new short[]{OpCodes.i2l});
		primitiveCastInstructions.put("D2B", new short[]{OpCodes.d2i, OpCodes.i2b});
		primitiveCastInstructions.put("D2C", new short[]{OpCodes.d2i, OpCodes.i2c});
		primitiveCastInstructions.put("D2F", new short[]{OpCodes.d2f});
		primitiveCastInstructions.put("D2I", new short[]{OpCodes.d2i});
		primitiveCastInstructions.put("D2J", new short[]{OpCodes.d2l});
		primitiveCastInstructions.put("D2S", new short[]{OpCodes.d2i, OpCodes.i2s});
		primitiveCastInstructions.put("D2Z", new short[]{OpCodes.d2i});
		primitiveCastInstructions.put("F2B", new short[]{OpCodes.f2i, OpCodes.i2b});
		primitiveCastInstructions.put("F2C", new short[]{OpCodes.f2i, OpCodes.i2c});
		primitiveCastInstructions.put("F2D", new short[]{OpCodes.f2d});
		primitiveCastInstructions.put("F2J", new short[]{OpCodes.f2l});
		primitiveCastInstructions.put("F2S", new short[]{OpCodes.f2i, OpCodes.i2s});
		primitiveCastInstructions.put("F2Z", new short[]{OpCodes.f2i});
		primitiveCastInstructions.put("F2Z", new short[]{OpCodes.f2i});
		primitiveCastInstructions.put("I2B", new short[]{OpCodes.i2b});
		primitiveCastInstructions.put("I2C", new short[]{OpCodes.i2c});
		primitiveCastInstructions.put("I2D", new short[]{OpCodes.i2d});
		primitiveCastInstructions.put("I2F", new short[]{OpCodes.i2f});
		primitiveCastInstructions.put("I2J", new short[]{OpCodes.i2l});
		primitiveCastInstructions.put("I2S", new short[]{OpCodes.i2s});
		primitiveCastInstructions.put("J2B", new short[]{OpCodes.l2i, OpCodes.i2b});
		primitiveCastInstructions.put("J2C", new short[]{OpCodes.l2i, OpCodes.i2c});
		primitiveCastInstructions.put("J2D", new short[]{OpCodes.l2d});
		primitiveCastInstructions.put("J2F", new short[]{OpCodes.l2f});
		primitiveCastInstructions.put("J2I", new short[]{OpCodes.l2i});
		primitiveCastInstructions.put("J2S", new short[]{OpCodes.l2i, OpCodes.i2s});
		primitiveCastInstructions.put("J2Z", new short[]{OpCodes.l2i});
		primitiveCastInstructions.put("S2B", new short[]{OpCodes.i2b});
		primitiveCastInstructions.put("S2C", new short[]{OpCodes.i2c});
		primitiveCastInstructions.put("S2D", new short[]{OpCodes.i2d});
		primitiveCastInstructions.put("S2F", new short[]{OpCodes.i2f});
		primitiveCastInstructions.put("S2J", new short[]{OpCodes.i2l});
		primitiveCastInstructions.put("Z2B", new short[]{OpCodes.i2b});
		primitiveCastInstructions.put("Z2C", new short[]{OpCodes.i2c});
		primitiveCastInstructions.put("Z2D", new short[]{OpCodes.i2d});
		primitiveCastInstructions.put("Z2F", new short[]{OpCodes.i2f});
		primitiveCastInstructions.put("Z2J", new short[]{OpCodes.i2l});
		
		primitiveToBox = new HashMap<String, String>();
		primitiveToBox.put("B", "java/lang/Byte");
		primitiveToBox.put("C", "java/lang/Character");
		primitiveToBox.put("D", "java/lang/Double");
		primitiveToBox.put("F", "java/lang/Float");
		primitiveToBox.put("I", "java/lang/Integer");
		primitiveToBox.put("J", "java/lang/Long");
		primitiveToBox.put("S", "java/lang/Short");
		primitiveToBox.put("Z", "java/lang/Boolean");
		
		boxToPrimitive = new HashMap<String, String>();
		
		for (Entry<String, String> e: primitiveToBox.entrySet()) {
			boxToPrimitive.put(e.getValue(), e.getKey());
		}
		
	}

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
		AbstractInstruction result =  new ArgumentLessInstruction(opCode);
		result.setResolved(true);
		return result;
	}
	
	protected AbstractInstruction createBipushInstruction(byte value) {
		AbstractInstruction result = new BipushInstruction(value);
		result.setResolved(true);
		return result;
	}
	
	protected AbstractInstruction createSipushInstruction(short value) {
		AbstractInstruction result = new SipushInstruction(value);
		result.setResolved(true);
		return result;
	}
	
	protected AbstractInstruction createLdcInstruction(AbstractConstantPoolEntry cp) {
		AbstractInstruction result = new LdcInstruction(cp);
		result.setResolved(true);
		return result;
	}
	
	protected AbstractInstruction createLocalVariableInstruction(short opCode, LocalVariable var) {
		LocalVariableInstruction instr = new LocalVariableInstruction(opCode, var);
		ShortLocalVariableInstruction shortVar = instr.createShortReplacement();
		AbstractInstruction result = null;
		if (shortVar == null) {
			result =  instr;
		} else {
			result =  shortVar;
		}
		result.setResolved(true);
		return result;
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
		AbstractInstruction result =  new ConstantPoolInstruction(opCode, constant);
		result.setResolved(true);
		return result;
	}
	
	protected AbstractInstruction createBranchInstruction(short opCode, SymbolReference ref) {
		AbstractInstruction result =  new BranchInstruction(opCode, ref);
		result.setResolved(false);
		return result;
	}
	
	
	protected AbstractInstruction createBranchInstruction(short opCode, AbstractInstruction instr, boolean afterTarget) {
		BranchInstruction result = new BranchInstruction(opCode,  instr);
		result.setResolved(!afterTarget);
		result.setAfterTarget(afterTarget);
		return result;
	}
	
	protected AbstractInstruction createBranchInstruction(short opCode, AbstractInstruction instr) {
		return createBranchInstruction(opCode, instr, false);
	}
	
	protected ClassInfo getClassInfo(String className) {
		return instructions.getConstantPool().getOrAddClassInfo(className);
	}
	
	protected FieldrefInfo getFieldRefInfo(String className, String name, TypeDescriptor type) {
		return instructions.getConstantPool().getOrAddFieldrefInfo(className, name, type.getValue());
	}
	
	protected MethodrefInfo getMethodRefInfo(String className, String name, MethodDescriptor type) {
		return instructions.getConstantPool().getOrAddMethofdrefInfo(className, name, type.getValue());
	}
	
	protected InterfaceMethodrefInfo getInterfaceMethodRefInfo(String className, String name, MethodDescriptor type) {
		return instructions.getConstantPool().getOrAddInterfaceMethofdrefInfo(className, name, type.getValue());
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
		TypeDescriptor type =  argumentTypes.get(arg);
		if (type == null) {
			return null;
		} else {
			TypeDescriptor castType = castTypes.get(arg);
			if (castType != null) {
				return castType;
			} else {
				return type;
			}
		}
	}
	
	protected int getNumberOfArguments() {
		return arguments.size();
	}
	
	@Override
	public boolean resolve() {
		argumentTypes = new HashMap<IMacroArgument, TypeDescriptor>();
		castTypes = new HashMap<IMacroArgument, TypeDescriptor>();
		constantArguments = new HashMap<IMacroArgument, AbstractConstantPoolEntry>();
		variableArguments = new HashMap<IMacroArgument, LocalVariable>();
		instructionReferences = new HashMap<IMacroArgument, AbstractInstruction>();
		localFieldReferences = new HashMap<IMacroArgument, Field>();
		localMethodReferences = new HashMap<IMacroArgument, Method>();
		for (int i=0;i<arguments.size(); i++) {
			IMacroArgument arg = arguments.get(i);
			TypeDescriptor type = resolveArgumentType(arg);
			argumentTypes.put(arg, type);
			if (type != null) {
				JavaTypeLiteral castTypeLiteral = call.getCasttypes().get(arg);
				if (castTypeLiteral != null && castTypeLiteral.getDescriptor() != null) {
					TypeDescriptor castType = castTypeLiteral.getDescriptor();
					if (castType != null) {
						if (canCast(type, castType)) {
							castTypes.put(arg, castTypeLiteral.getDescriptor());
						} else {
							emitError(castTypeLiteral.getSourceLocation(), "can not cast "+type.getValue()+" to "+castType.getValue());
						}
					}
				}
			}	
			if (arg instanceof SymbolReference) {
				Object ref = resolveSymbolReference(arg, true);
				if (ref !=null) {
					if (ref instanceof AbstractConstantPoolEntry) {
						constantArguments.put(arg, (AbstractConstantPoolEntry)ref);
					} else if (ref instanceof LocalVariable) {
						variableArguments.put(arg, (LocalVariable)ref);
					} else if (ref instanceof Field) {
						localFieldReferences.put(arg, (Field)ref);
					} else if (ref instanceof Method) {
						localMethodReferences.put(arg, (Method)ref);	
					} else {
						instructionReferences.put(arg, (AbstractInstruction)ref);
					}
				}
			} 
		}
		if (!hasError) {
			for (int i=0;i<arguments.size(); i++) {
				IMacroArgument arg = arguments.get(i);
				if (!argumentTypes.containsKey(arg) && !validateSpecialArgumentType(i, arg)) {
					emitError(arg.getSourceLocation(), "wrong argument type");
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
	
	protected boolean isInstructionSymbolReference(IMacroArgument arg) {
		return instructionReferences.containsKey(arg);
	}
	
	protected boolean isLocalFieldSymbolReference(IMacroArgument arg) {
		return localFieldReferences.containsKey(arg);
	}
	
	protected boolean isLocalMethodSymbolReference(IMacroArgument arg) {
		return localMethodReferences.containsKey(arg);
	}
	
	protected boolean isFieldReference(IMacroArgument arg) {
		return isLocalFieldSymbolReference(arg) ||
				(isConstantSymbolReference(arg) && constantArguments.get(arg) instanceof FieldrefInfo);
	}
	
	protected boolean isMethodReference(IMacroArgument arg) {
		return isLocalMethodSymbolReference(arg) ||
				(isConstantSymbolReference(arg) && (
						(constantArguments.get(arg) instanceof MethodrefInfo) || (constantArguments.get(arg) instanceof InterfaceMethodrefInfo)));
	}
	
	protected Field getLocalFieldSymbolRefferenceValue(IMacroArgument arg) {
		if (!localFieldReferences.containsKey(arg)) {
			throw new IllegalArgumentException("unknown field "+arg);
		}
		return localFieldReferences.get(arg);
	}
	
	protected Method getLocalMethodSymbolRefferenceValue(IMacroArgument arg) {
		if (!localMethodReferences.containsKey(arg)) {
			throw new IllegalArgumentException("unknown method "+arg);
		}
		return localMethodReferences.get(arg);
	}
	
	private Object resolveSymbolReference(IMacroArgument arg, boolean emitError) {
		List<Object> results = new ArrayList<Object>();
		SymbolReference sym = (SymbolReference)arg;
		LocalVariable var = instructions.getVariablesPool().getByName(sym.getSymbolName());
		AbstractInstruction instr = (AbstractInstruction)instructions.getSymbolTable().get(sym.getSymbolName());
		if (var !=null) {
			results.add(var);
		}
		if (instr != null) {
			results.add(instr);
		}
		if (results.size()>0) {
			
		} else {
			Clazz clazz = instructions.getAncestor(Clazz.class);
			if (clazz.getMethods().getMethodsByName(sym.getSymbolName()).size()>0) {
				results.addAll(clazz.getMethods().getMethodsByName(sym.getSymbolName()));
			}
			if (clazz.getFields().getFieldsByName(sym.getSymbolName()) != null) {
				results.addAll(clazz.getFields().getFieldsByName(sym.getSymbolName()));
			}
			
			AbstractConstantPoolEntry entry = (AbstractConstantPoolEntry)  instructions.getConstantPool().getSymbolTable().get(sym.getSymbolName());
			if (entry != null) {
				results.add(entry);
			}
		}
		
		if (results.size() == 0) {
			if (emitError) {
				emitError(arg.getSourceLocation(), "unknown name "+sym.getSymbolName());
			} else {
				throw new RuntimeException("unknown name: "+sym.getSymbolName());
			}
			
		} else if (results.size()>1) {
			if (emitError) {
				emitError(arg.getSourceLocation(), "ambiguous name "+sym.getSymbolName());
			} else {
				throw new RuntimeException("ambiguous name: "+sym.getSymbolName());
			}
		} 
		return results.get(0);
	}
	
	private TypeDescriptor resolveArgumentType(IMacroArgument arg) {
		if (isLiteral(arg)) {
			return getLiteralType(arg);
		} else if (isSymbolReference(arg)) {
			Object ref = resolveSymbolReference(arg, true);
			if (ref != null) {
				if (ref instanceof LocalVariable) {
					return getVariableType((LocalVariable)ref);
				} else if (ref instanceof AbstractConstantPoolEntry){
					return getConstantType((AbstractConstantPoolEntry)ref);
				} else {
					return null;
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
		result.add(createConstantPoolInstruction(OpCodes.ldc2_w, ii));
		return result;
	}
	
	protected List<AbstractInstruction> pushDoubleArgument(IMacroArgument arg, List<AbstractInstruction> result) {
		double value = ((DoubleLiteral)arg).getValue();
		return pushDoubleValue(value, result);
	}
	
	protected List<AbstractInstruction> pushDoubleValue(double value, List<AbstractInstruction> result) {
		DoubleInfo ii = getDoubleInfo(value);
		result.add(createConstantPoolInstruction(OpCodes.ldc2_w, ii));
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
		String value = ((StringLiteral)arg).getStringValue();
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
		if (entry instanceof LongInfo || entry instanceof DoubleInfo) {
			result.add(createConstantPoolInstruction(OpCodes.ldc2_w, entry));
		} else {
			result.add(createLdcInstruction(entry));
		}
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
		} else {
			throw new IllegalArgumentException("Unknown combination: "+arg+"; "+type);
		}
		
		TypeDescriptor castType = castTypes.get(arg);
		if (castType != null) {
			cast(type, castType, result);
		}
	}
	
	protected void pushValueFromField(IMacroArgument object, IMacroArgument field, List<AbstractInstruction> result) {
		
		if (object != null) {
			pushArgument(object, result);
		}
		short opCode = (object == null)?OpCodes.getstatic:OpCodes.getfield;
		if (isConstantSymbolReference(field)) {
			result.add(createConstantPoolInstruction(opCode, constantArguments.get(field)));
		} else {
			throw new IllegalArgumentException("field: "+field);
		}
	}
	
	protected abstract boolean validateSpecialArgumentType(int index, IMacroArgument arg);
	protected abstract boolean doResolve();

	public boolean hasError() {
		return hasError;
	}
	
	//Cast Methods
	
	private boolean isBoxType(TypeDescriptor desc) {
		return desc.isObject() && boxToPrimitive.containsKey(desc.getClassName());
	}
	
	protected boolean canCast(TypeDescriptor t1, TypeDescriptor t2) {
		if (t1.isPrimitive() && t2.isPrimitive()) {
			return true;
		} else if (t1.isPrimitive() && !t2.isPrimitive()) {
			return isBoxType(t2) || (t2.isObject() && t2.getClassName().equals("java/lang/Number"));
		} else if (t2.isPrimitive() && !t1.isPrimitive()) {
			return isBoxType(t1) || (t1.isObject() && t1.getClassName().equals("java/lang/Number"));
		} else {
			return true;
		}
	}
	
	protected List<AbstractInstruction> cast(TypeDescriptor t1, TypeDescriptor t2, List<AbstractInstruction> result) {
		if (t1.isPrimitive() && t2.isPrimitive()) {
			short[] opCodes = primitiveCastInstructions.get(t1.getValue()+"2"+t2.getValue());
			if (opCodes != null) {
				for (short code: opCodes) {
					result.add(createArgumentLessInstruction(code));
				}
			}
		} else if (t1.isPrimitive() && !t2.isPrimitive()) {
			throw new NotImplementedException("");
		} else if (t2.isPrimitive() && !t1.isPrimitive()) {
			throw new NotImplementedException("");
		} else {
			throw new NotImplementedException("");
		}
		
		return result;
	}
	

}
