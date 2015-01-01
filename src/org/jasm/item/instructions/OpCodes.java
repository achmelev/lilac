package org.jasm.item.instructions;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpCodes {
	
	private static Map<String, Short> nameToOpcode = new HashMap<>();
	private static Map<Short, String> opcodeToName = new HashMap<>();
	private static List<Short> argumentLessInstructions = new ArrayList<>();
	private static List<Short> localVariableInstructions = new ArrayList<>();
	private static List<Short> shortLocalVariableInstructions = new ArrayList<>();
	private static List<Short> constantPoolInstructions = new ArrayList<>();
	private static List<Short> branchInstructions = new ArrayList<>();
	private static List<Short> wideBranchInstructions = new ArrayList<>();
	private static List<Short> specialInstructions = new ArrayList<>();
	private static List<Short> wideInstructionsFormat1 = new ArrayList<>();
	
	public static short aaload = 0x32;
	public static short aastore = 0x53;
	public static short aconst_null= 0x1;
	public static short aload = 0x19;
	public static short aload_0= 0x2a;
	public static short aload_1= 0x2b;
	public static short aload_2= 0x2c;
	public static short aload_3= 0x2d;
	public static short anewarray= 0xbd;
	public static short areturn= 0xb0;
	public static short arraylength= 0xbe;
	public static short astore= 0x3a;
	public static short astore_0 = 0x4b;
	public static short astore_1 = 0x4c;
	public static short astore_2 = 0x4d;
	public static short astore_3 = 0x4e;
	public static short athrow = 0xbf;
	public static short baload= 0x33;
	public static short bastore = 0x54;
	public static short bipush= 0x10;
	public static short caload= 0x34;
	public static short castore= 0x55;
	public static short checkcast= 0xc0;
	public static short d2f= 0x90;
	public static short d2i= 0x8e;
	public static short d2l= 0x8f;
	public static short dadd = 0x63;
	public static short daload= 0x31;
	public static short dastore = 0x52;
	public static short dcmpg  = (0x98);
	public static short dcmpl= 0x97;
	public static short dconst_0  = 0xe;
	public static short dconst_1 = 0xf;
	public static short ddiv= 0x6f;
	public static short dload= 0x18;
	public static short dload_0= 0x26;
	public static short dload_1= 0x27;
	public static short dload_2= 0x28;
	public static short dload_3= 0x29;
	public static short dmul= 0x6b;
	public static short dneg = 0x77;
	public static short drem = 0x73;
	public static short dreturn = 0xaf;
	public static short dstore = 0x39;
	public static short dstore_0  = 0x47;
	public static short dstore_1  = 0x48;
	public static short dstore_2  = 0x49;
	public static short dstore_3  = 0x4a;
	public static short dsub= 0x67;
	public static short dup = 0x59;
	public static short dup_x1= 0x5a;
	public static short dup_x2= 0x5b;
	public static short dup2= 0x5c;
	public static short dup2_x1= 0x5d;
	public static short dup2_x2= 0x5e;
	public static short f2d= 0x8d;
	public static short f2i= 0x8b;
	public static short f2l = 0x8c;
	public static short fadd= 0x62;
	public static short faload = 0x30;
	public static short fastore = 0x51;
	public static short fcmpg = 0x96;
	public static short fcmpl  = 0x95;
	public static short fconst_0= 0xb;
	public static short fconst_1= 0xc;
	public static short fconst_2= 0xd;
	public static short fdiv= 0x6e;
	public static short fload= 0x17;
	public static short fload_0= 0x22;
	public static short fload_1= 0x23;
	public static short fload_2= 0x24;
	public static short fload_3= 0x25;
	public static short fmul= 0x6a;
	public static short fneg= 0x76;
	public static short frem= 0x72;
	public static short freturn= 0xae;
	public static short fstore = 0x38;
	public static short fstore_0 = 0x43;
	public static short fstore_1 = 0x44;
	public static short fstore_2 = 0x45;
	public static short fstore_3 = 0x46;
	public static short fsub = 0x66;
	public static short getfield = 0xb4;
	public static short getstatic = 0xb2;
	public static short goto_ = 0xa7;
	public static short goto_w = 0xc8;
	public static short i2b= 0x91;
	public static short i2c= 0x92;
	public static short i2d= 0x87;
	public static short i2f= 0x86;
	public static short i2l= 0x85;
	public static short i2s= 0x93;
	public static short iadd = 0x60;
	public static short iaload= 0x2e;
	public static short iand = 0x7e;
	public static short iastore  = 0x4f;
	public static short iconst_m1= 0x2;
	public static short iconst_0  = 0x3;
	public static short iconst_1  = 0x4;
	public static short iconst_2  = 0x5;
	public static short iconst_3  = 0x6;
	public static short iconst_4  = 0x7;
	public static short iconst_5  = 0x8;
	public static short idiv= 0x6c;
	public static short if_acmpeq= 0xa5;
	public static short if_acmpne= 0xa6;
	public static short if_icmpeq = 0x9f;
	public static short if_icmpne= 0xa0;
	public static short if_icmplt = 0xa1;
	public static short if_icmpge = 0xa2;
	public static short if_icmpgt = 0xa3;
	public static short if_icmple = 0xa4;
	public static short ifeq = 0x99;
	public static short ifne = 0x9a;
	public static short iflt = 0x9b;
	public static short ifge  = 0x9c;
	public static short ifgt   = 0x9d;
	public static short ifle  = 0x9e;
	public static short ifnonnull  = 0xc7;
	public static short ifnull= 0xc6;
	public static short iinc = 0x84;
	public static short iload= 0x15;
	public static short iload_0 = 0x1a;
	public static short iload_1 = 0x1b;
	public static short iload_2 = 0x1c;
	public static short iload_3 = 0x1d;
	public static short imul= 0x68;
	public static short ineg= 0x74;
	public static short instanceof_= 0xc1;
	public static short invokedynamic= 0xba;
	public static short invokeinterface= 0xb9;
	public static short invokespecial = 0xb7;
	public static short invokestatic= 0xb8;
	public static short invokevirtual = 0xb6;
	public static short ior = 0x80;
	public static short irem= 0x70;
	public static short ireturn = 0xac;
	public static short ishl= 0x78;
	public static short ishr= 0x7a;
	public static short istore= 0x36;
	public static short istore_0= 0x3b;
	public static short istore_1= 0x3c;
	public static short istore_2= 0x3d;
	public static short istore_3= 0x3e;
	public static short isub= 0x64;
	public static short iushr= 0x7c;
	public static short ixor= 0x82;
	public static short jsr= 0xa8;
	public static short jsr_w = 0xc9;
	public static short l2d = 0x8a;
	public static short l2f = 0x89;
	public static short l2i = 0x88;
	public static short ladd = 0x61;
	public static short laload= 0x2f;
	public static short land = 0x7f;
	public static short lastore= 0x50;
	public static short lcmp= 0x94;
	public static short lconst_0= 0x9;
	public static short lconst_1= 0xa;
	public static short ldc= 0x12;
	public static short ldc_w= 0x13;
	public static short ldc2_w = 0x14;
	public static short ldiv= 0x6d;
	public static short lload = 0x16;
	public static short lload_0   = 0x1e;
	public static short lload_1   = 0x1f;
	public static short lload_2   = 0x20;
	public static short lload_3   = 0x21;
	public static short lmul= 0x69;
	public static short lneg  = 0x75;
	public static short lookupswitch = 0xab;
	public static short lor= 0x81;
	public static short lrem= 0x71;
	public static short lreturn= 0xad;
	public static short lshl= 0x79;
	public static short lshr= 0x7b;
	public static short lstore= 0x37;
	public static short lstore_0 = 0x3f;
	public static short lstore_1 = 0x40;
	public static short lstore_2 = 0x41;
	public static short lstore_3 = 0x42;
	public static short lsub = 0x65;
	public static short lushr = 0x7d;
	public static short lxor = 0x83;
	public static short monitorenter = 0xc2;
	public static short monitorexit  = 0xc3;
	public static short multianewarray  = 0xc5;
	public static short new_  = 0xbb;
	public static short newarray  = 0xbc;
	public static short nop= 0x0;
	public static short pop = 0x57;
	public static short pop2= 0x58;
	public static short putfield= 0xb5;
	public static short putstatic= 0xb3;
	public static short ret= 0xa9;
	public static short return_= 0xb1;
	public static short saload= 0x35;
	public static short sastore= 0x56;
	public static short sipush = 0x11;
	public static short swap= 0x5f;
	public static short tableswitch = 0xaa;
	public static short wide = 0xc4;
	
	
	private static boolean _initialized = false;
	
	private static void initialize() {
		
		if (!_initialized) {
			Class cl = null;
			try {
				cl = Class.forName("org.jasm.item.instructions.OpCodes");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			
			Field [] fields = cl.getDeclaredFields();
			for (Field f: fields) {
				if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(Short.TYPE)) {
					String name = f.getName();
					if (name.endsWith("_")) {
						name = name.substring(0, name.length()-1);
					}
					short value = 0;
					try {
						value = f.getShort(name);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					if (nameToOpcode.containsKey(name)) {
						throw new IllegalStateException(name+" is already there!");
					}
					nameToOpcode.put(name, value);
					if (opcodeToName.containsKey(value)) {
						throw new IllegalStateException(value+" is already there!");
					}
					opcodeToName.put(value, name);
					
				}
			}
			
			//bipush,iinc,invokedynamic,invokeinterface,ldc,lookupswitch,multinewarray,newarray,sipush,tableswitch, wide
			specialInstructions.add(bipush);
			specialInstructions.add(iinc);
			specialInstructions.add(invokedynamic);
			specialInstructions.add(invokeinterface);
			specialInstructions.add(ldc);
			specialInstructions.add(lookupswitch);
			specialInstructions.add(multianewarray);
			specialInstructions.add(newarray);
			specialInstructions.add(sipush);
			specialInstructions.add(tableswitch);
			specialInstructions.add(wide);
			
			//Argumentless Instructions
			argumentLessInstructions.add(aaload);
			argumentLessInstructions.add(aastore);
			argumentLessInstructions.add(aconst_null);
			argumentLessInstructions.add(areturn);
			argumentLessInstructions.add(arraylength);
			argumentLessInstructions.add(athrow);
			argumentLessInstructions.add(baload);
			argumentLessInstructions.add(bastore);
			argumentLessInstructions.add(caload);
			argumentLessInstructions.add(castore);
			argumentLessInstructions.add(d2f);
			argumentLessInstructions.add(d2i);
			argumentLessInstructions.add(d2l);
			argumentLessInstructions.add(dadd);
			argumentLessInstructions.add(daload);
			argumentLessInstructions.add(dastore);
			argumentLessInstructions.add(dcmpg);
			argumentLessInstructions.add(dcmpl);
			argumentLessInstructions.add(dconst_0);
			argumentLessInstructions.add(dconst_1);
			argumentLessInstructions.add(ddiv);
			argumentLessInstructions.add(dmul);
			argumentLessInstructions.add(dneg);
			argumentLessInstructions.add(drem);
			argumentLessInstructions.add(dreturn);
			argumentLessInstructions.add(dsub);
			argumentLessInstructions.add(dup);
			argumentLessInstructions.add(dup_x1);
			argumentLessInstructions.add(dup_x2);
			argumentLessInstructions.add(dup2);
			argumentLessInstructions.add(dup2_x1);
			argumentLessInstructions.add(dup2_x2);
			argumentLessInstructions.add(f2d);
			argumentLessInstructions.add(f2i);
			argumentLessInstructions.add(f2l);
			argumentLessInstructions.add(fadd);
			argumentLessInstructions.add(faload);
			argumentLessInstructions.add(fastore);
			argumentLessInstructions.add(fcmpg);
			argumentLessInstructions.add(fcmpl);
			argumentLessInstructions.add(fconst_0);
			argumentLessInstructions.add(fconst_1);
			argumentLessInstructions.add(fconst_2);
			argumentLessInstructions.add(fdiv);
			argumentLessInstructions.add(fmul);
			argumentLessInstructions.add(fneg);
			argumentLessInstructions.add(frem);
			argumentLessInstructions.add(freturn);
			argumentLessInstructions.add(fsub);
			argumentLessInstructions.add(i2b);
			argumentLessInstructions.add(i2c);
			argumentLessInstructions.add(i2d);
			argumentLessInstructions.add(i2f);
			argumentLessInstructions.add(i2l);
			argumentLessInstructions.add(i2s);
			argumentLessInstructions.add(iadd);
			argumentLessInstructions.add(iaload);
			argumentLessInstructions.add(iand);
			argumentLessInstructions.add(iastore);
			argumentLessInstructions.add(iconst_m1);
			argumentLessInstructions.add(iconst_0);
			argumentLessInstructions.add(iconst_1);
			argumentLessInstructions.add(iconst_2);
			argumentLessInstructions.add(iconst_3);
			argumentLessInstructions.add(iconst_4);
			argumentLessInstructions.add(iconst_5);
			argumentLessInstructions.add(idiv);
			argumentLessInstructions.add(imul);
			argumentLessInstructions.add(ineg);
			argumentLessInstructions.add(ior);
			argumentLessInstructions.add(irem);
			argumentLessInstructions.add(ireturn);
			argumentLessInstructions.add(ishl);
			argumentLessInstructions.add(ishr);
			argumentLessInstructions.add(isub);
			argumentLessInstructions.add(iushr);
			argumentLessInstructions.add(ixor);
			argumentLessInstructions.add(l2d);
			argumentLessInstructions.add(l2f);
			argumentLessInstructions.add(l2i);
			argumentLessInstructions.add(ladd);
			argumentLessInstructions.add(laload);
			argumentLessInstructions.add(land);
			argumentLessInstructions.add(lastore);
			argumentLessInstructions.add(lcmp);
			argumentLessInstructions.add(lconst_0);
			argumentLessInstructions.add(lconst_1);
			argumentLessInstructions.add(ldiv);
			argumentLessInstructions.add(lmul);
			argumentLessInstructions.add(lneg);
			argumentLessInstructions.add(lor);
			argumentLessInstructions.add(lrem);
			argumentLessInstructions.add(lreturn);
			argumentLessInstructions.add(lshl);
			argumentLessInstructions.add(lshr);
			argumentLessInstructions.add(lsub);
			argumentLessInstructions.add(lushr);
			argumentLessInstructions.add(lxor);
			argumentLessInstructions.add(monitorenter);
			argumentLessInstructions.add(monitorexit);
			argumentLessInstructions.add(nop);
			argumentLessInstructions.add(pop);
			argumentLessInstructions.add(pop2);
			argumentLessInstructions.add(return_);
			argumentLessInstructions.add(saload);
			argumentLessInstructions.add(sastore);
			argumentLessInstructions.add(swap);
			
			//Instruction with local variable as argument
			localVariableInstructions.add(aload);
			localVariableInstructions.add(astore);
			localVariableInstructions.add(dload);
			localVariableInstructions.add(dstore);
			localVariableInstructions.add(fload);
			localVariableInstructions.add(fstore);
			localVariableInstructions.add(iload);
			localVariableInstructions.add(istore);
			localVariableInstructions.add(lload);
			localVariableInstructions.add(lstore);
			localVariableInstructions.add(ret);
			
			//Short-Versions of the local variable instructions (without argument)
			shortLocalVariableInstructions.add(aload_0);
			shortLocalVariableInstructions.add(aload_1);
			shortLocalVariableInstructions.add(aload_2);
			shortLocalVariableInstructions.add(aload_3);
			shortLocalVariableInstructions.add(astore_0);
			shortLocalVariableInstructions.add(astore_1);
			shortLocalVariableInstructions.add(astore_2);
			shortLocalVariableInstructions.add(astore_3);
			shortLocalVariableInstructions.add(dload_0);
			shortLocalVariableInstructions.add(dload_1);
			shortLocalVariableInstructions.add(dload_2);
			shortLocalVariableInstructions.add(dload_3);
			shortLocalVariableInstructions.add(dstore_0);
			shortLocalVariableInstructions.add(dstore_1);
			shortLocalVariableInstructions.add(dstore_2);
			shortLocalVariableInstructions.add(dstore_3);
			shortLocalVariableInstructions.add(fload_0);
			shortLocalVariableInstructions.add(fload_1);
			shortLocalVariableInstructions.add(fload_2);
			shortLocalVariableInstructions.add(fload_3);
			shortLocalVariableInstructions.add(fstore_0);
			shortLocalVariableInstructions.add(fstore_1);
			shortLocalVariableInstructions.add(fstore_2);
			shortLocalVariableInstructions.add(fstore_3);
			shortLocalVariableInstructions.add(iload_0);
			shortLocalVariableInstructions.add(iload_1);
			shortLocalVariableInstructions.add(iload_2);
			shortLocalVariableInstructions.add(iload_3);
			shortLocalVariableInstructions.add(istore_0);
			shortLocalVariableInstructions.add(istore_1);
			shortLocalVariableInstructions.add(istore_2);
			shortLocalVariableInstructions.add(istore_3);
			shortLocalVariableInstructions.add(lload_0);
			shortLocalVariableInstructions.add(lload_1);
			shortLocalVariableInstructions.add(lload_2);
			shortLocalVariableInstructions.add(lload_3);
			shortLocalVariableInstructions.add(lstore_0);
			shortLocalVariableInstructions.add(lstore_1);
			shortLocalVariableInstructions.add(lstore_2);
			shortLocalVariableInstructions.add(lstore_3);
			
			
			
			
			//Instructions with constants from constant pool as argument 
			constantPoolInstructions.add(anewarray);
			constantPoolInstructions.add(checkcast);
			constantPoolInstructions.add(getfield);
			constantPoolInstructions.add(getstatic);
			constantPoolInstructions.add(instanceof_);
			constantPoolInstructions.add(invokespecial);
			constantPoolInstructions.add(invokestatic);
			constantPoolInstructions.add(invokevirtual);
			constantPoolInstructions.add(ldc_w);
			constantPoolInstructions.add(ldc2_w);
			constantPoolInstructions.add(new_);
			constantPoolInstructions.add(putfield);
			constantPoolInstructions.add(putstatic);
			
			//branch instructions
			branchInstructions.add(goto_);
			branchInstructions.add(if_acmpeq);
			branchInstructions.add(if_acmpne);
			branchInstructions.add(if_icmpeq);
			branchInstructions.add(if_icmpge);
			branchInstructions.add(if_icmpgt);
			branchInstructions.add(if_icmple);
			branchInstructions.add(if_icmplt);
			branchInstructions.add(if_icmpne);
			branchInstructions.add(ifeq);
			branchInstructions.add(ifge);
			branchInstructions.add(ifgt);
			branchInstructions.add(ifle);
			branchInstructions.add(iflt);
			branchInstructions.add(ifne);
			branchInstructions.add(ifnonnull);
			branchInstructions.add(ifnull);
			branchInstructions.add(jsr);
			
			//wide branch instructions
			wideBranchInstructions.add(goto_w);
			wideBranchInstructions.add(jsr_w);
			
			//
			wideInstructionsFormat1.add(OpCodes.aload);
			wideInstructionsFormat1.add(OpCodes.astore);
			wideInstructionsFormat1.add(OpCodes.dload);
			wideInstructionsFormat1.add(OpCodes.dstore);
			wideInstructionsFormat1.add(OpCodes.fload);
			wideInstructionsFormat1.add(OpCodes.fstore);
			wideInstructionsFormat1.add(OpCodes.iload);
			wideInstructionsFormat1.add(OpCodes.istore);
			wideInstructionsFormat1.add(OpCodes.lload);
			wideInstructionsFormat1.add(OpCodes.lstore);
			
			_initialized = true;
		}
	}
	
	public static short getOpcodeForName(String name) {
		initialize();
		if (nameToOpcode.containsKey(name)) {
			return nameToOpcode.get(name);
		} else {
			throw new IllegalArgumentException("Unknown name: "+name);
		}
	}
	
	public static String getNameForOpcode(short opcode) {
		initialize();
		if (opcodeToName.containsKey(opcode)) {
			return opcodeToName.get(opcode);
		} else {
			throw new IllegalArgumentException("Unknown op code: "+Integer.toHexString(opcode));
		}
	}
	
	public static int getNumberOfOpcodes() {
		initialize();
		if (opcodeToName.keySet().size() != nameToOpcode.keySet().size()) {
			throw new IllegalStateException(opcodeToName.keySet().size()+":"+nameToOpcode.keySet().size());
		}
		return opcodeToName.keySet().size();
	}
	
	public static List<String> getNames() {
		List<String> result = new ArrayList<>();
		result.addAll(nameToOpcode.keySet());
		return result;
	}

	public static List<Short> getArgumentLessInstructions() {
		initialize();
		return argumentLessInstructions;
	}
	
	public static List<Short> getBranchInstructions() {
		initialize();
		return branchInstructions;
	}

	public static List<Short> getLocalVariableInstructions() {
		initialize();
		return localVariableInstructions;
	}
	
	public static List<Short> getShortLocalVariableInstructions() {
		initialize();
		return shortLocalVariableInstructions;
	}

	public static List<Short> getConstantPoolInstructions() {
		initialize();
		return constantPoolInstructions;
	}

	public static List<Short> getWideBranchInstructions() {
		initialize();
		return wideBranchInstructions;
	}

	public static List<Short> getSpecialInstructions() {
		initialize();
		return specialInstructions;
	}
	
	public static boolean isArgumentLessInstruction(short opCode) {
		initialize();
		return argumentLessInstructions.contains(opCode);
	}
	
	public static boolean isBranchInstruction(short opCode) {
		initialize();
		return branchInstructions.contains(opCode);
	}
	
	public static boolean isLocalVariableInstruction(short opCode) {
		initialize();
		return localVariableInstructions.contains(opCode);
	}
	
	public static boolean isShortLocalVariableInstruction(short opCode) {
		initialize();
		return shortLocalVariableInstructions.contains(opCode);
	}
	
	public static boolean isConstantPoolInstruction(short opCode) {
		initialize();
		return constantPoolInstructions.contains(opCode);
	}
	
	public static boolean isSpecialInstruction(short opCode) {
		initialize();
		return specialInstructions.contains(opCode);
	}
	
	public static boolean isWideBranchInstruction(short opCode) {
		initialize();
		return wideBranchInstructions.contains(opCode);
	}
	
	public static boolean isWideFormat1Instruction(short opCode) {
		initialize();
		return wideInstructionsFormat1.contains(opCode);
	}

	public static void main(String[] args) {
		initialize();
		
		System.out.println("//Instructions");
		List<String> opNames = new ArrayList<String>();
		opNames.addAll(opcodeToName.values());
		Collections.sort(opNames);
		for (String n: opNames) {
			if (!shortLocalVariableInstructions.contains(nameToOpcode.get(n))) {
				System.out.println(n.toUpperCase()+": '"+n+"';");
			}
		}
		
		
		StringBuffer buf = new StringBuffer();
		String instrs = null;
		buf.append("argumentlessop: ");
		int index = 0;
		for (Short code: argumentLessInstructions) {
			if (!shortLocalVariableInstructions.contains(code)) {
				String name = opcodeToName.get(code);
				if (name == null) {
					throw new IllegalStateException("Something has gone wrong!");
				}
				if (index > 0) {
					buf.append("|");
				}
				buf.append(name.toUpperCase());
				index++;
			}
		}
		buf.append(";");
		instrs =buf.toString();
		System.out.println();
		System.out.println(instrs);
		
		buf.delete(0, buf.length());
		buf.append("constantpoolop: ");
		buf.append(opcodeToName.get(OpCodes.ldc).toUpperCase());
		for (Short code: constantPoolInstructions) {
				String name = opcodeToName.get(code);
				if (name == null) {
					throw new IllegalStateException("Something has gone wrong!");
				}
				buf.append("|");
				buf.append(name.toUpperCase());
				index++;
			
		}
		buf.append(";");
		instrs =buf.toString();
		System.out.println(instrs);
		
		buf.delete(0, buf.length());
		buf.append("localvarop: ");
		index = 0;
		for (Short code: localVariableInstructions) {
				String name = opcodeToName.get(code);
				if (name == null) {
					throw new IllegalStateException("Something has gone wrong!");
				}
				if (index > 0) {
					buf.append("|");
				}
				buf.append(name.toUpperCase());
				index++;
			
		}
		buf.append(";");
		instrs =buf.toString();
		System.out.println(instrs);
		
		buf.delete(0, buf.length());
		buf.append("branchop: ");
		index = 0;
		for (Short code: branchInstructions) {
				String name = opcodeToName.get(code);
				if (name == null) {
					throw new IllegalStateException("Something has gone wrong!");
				}
				if (index > 0) {
					buf.append("|");
				}
				buf.append(name.toUpperCase());
				index++;
			
		}
		buf.append(";");
		instrs =buf.toString();
		System.out.println(instrs);
	}
	
}
