package org.jasm.item.instructions.verify;

import java.lang.invoke.MethodHandleInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javassist.bytecode.Descriptor;

import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.InvokeDynamicInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.IRegisterIndexInstruction;
import org.jasm.item.instructions.IincInstruction;
import org.jasm.item.instructions.MultianewarrayInstruction;
import org.jasm.item.instructions.NewarrayInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.verify.types.ObjectValueType;
import org.jasm.item.instructions.verify.types.UninitializedValueType;
import org.jasm.item.instructions.verify.types.VerificationType;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.resolver.FieldInfo;
import org.jasm.resolver.MethodInfo;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;



public class Interpreter {
	
	private Verifier parent;
	
	public Frame execute(AbstractInstruction instr, Frame inputFrame) {

	  if (OpCodes.aaload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAaload(instr,inputFrame);
	  } else if (OpCodes.aastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAastore(instr,inputFrame);
	  } else if (OpCodes.aconst_null==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAconst_null(instr,inputFrame);
	  } else if (OpCodes.aload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAload(instr,inputFrame);
	  } else if (OpCodes.aload_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAload_0(instr,inputFrame);
	  } else if (OpCodes.aload_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAload_1(instr,inputFrame);
	  } else if (OpCodes.aload_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAload_2(instr,inputFrame);
	  } else if (OpCodes.aload_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAload_3(instr,inputFrame);
	  } else if (OpCodes.anewarray==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAnewarray(instr,inputFrame);
	  } else if (OpCodes.areturn==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAreturn(instr,inputFrame);
	  } else if (OpCodes.arraylength==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeArraylength(instr,inputFrame);
	  } else if (OpCodes.astore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAstore(instr,inputFrame);
	  } else if (OpCodes.astore_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAstore_0(instr,inputFrame);
	  } else if (OpCodes.astore_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAstore_1(instr,inputFrame);
	  } else if (OpCodes.astore_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAstore_2(instr,inputFrame);
	  } else if (OpCodes.astore_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAstore_3(instr,inputFrame);
	  } else if (OpCodes.athrow==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeAthrow(instr,inputFrame);
	  } else if (OpCodes.baload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeBaload(instr,inputFrame);
	  } else if (OpCodes.bastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeBastore(instr,inputFrame);
	  } else if (OpCodes.bipush==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeBipush(instr,inputFrame);
	  } else if (OpCodes.caload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeCaload(instr,inputFrame);
	  } else if (OpCodes.castore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeCastore(instr,inputFrame);
	  } else if (OpCodes.checkcast==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeCheckcast(instr,inputFrame);
	  } else if (OpCodes.d2f==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeD2f(instr,inputFrame);
	  } else if (OpCodes.d2i==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeD2i(instr,inputFrame);
	  } else if (OpCodes.d2l==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeD2l(instr,inputFrame);
	  } else if (OpCodes.dadd==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDadd(instr,inputFrame);
	  } else if (OpCodes.daload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDaload(instr,inputFrame);
	  } else if (OpCodes.dastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDastore(instr,inputFrame);
	  } else if (OpCodes.dcmpg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDcmpg(instr,inputFrame);
	  } else if (OpCodes.dcmpl==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDcmpl(instr,inputFrame);
	  } else if (OpCodes.dconst_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDconst_0(instr,inputFrame);
	  } else if (OpCodes.dconst_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDconst_1(instr,inputFrame);
	  } else if (OpCodes.ddiv==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDdiv(instr,inputFrame);
	  } else if (OpCodes.dload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDload(instr,inputFrame);
	  } else if (OpCodes.dload_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDload_0(instr,inputFrame);
	  } else if (OpCodes.dload_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDload_1(instr,inputFrame);
	  } else if (OpCodes.dload_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDload_2(instr,inputFrame);
	  } else if (OpCodes.dload_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDload_3(instr,inputFrame);
	  } else if (OpCodes.dmul==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDmul(instr,inputFrame);
	  } else if (OpCodes.dneg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDneg(instr,inputFrame);
	  } else if (OpCodes.drem==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDrem(instr,inputFrame);
	  } else if (OpCodes.dreturn==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDreturn(instr,inputFrame);
	  } else if (OpCodes.dstore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDstore(instr,inputFrame);
	  } else if (OpCodes.dstore_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDstore_0(instr,inputFrame);
	  } else if (OpCodes.dstore_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDstore_1(instr,inputFrame);
	  } else if (OpCodes.dstore_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDstore_2(instr,inputFrame);
	  } else if (OpCodes.dstore_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDstore_3(instr,inputFrame);
	  } else if (OpCodes.dsub==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDsub(instr,inputFrame);
	  } else if (OpCodes.dup==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup(instr,inputFrame);
	  } else if (OpCodes.dup2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup2(instr,inputFrame);
	  } else if (OpCodes.dup2_x1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup2_x1(instr,inputFrame);
	  } else if (OpCodes.dup2_x2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup2_x2(instr,inputFrame);
	  } else if (OpCodes.dup_x1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup_x1(instr,inputFrame);
	  } else if (OpCodes.dup_x2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeDup_x2(instr,inputFrame);
	  } else if (OpCodes.f2d==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeF2d(instr,inputFrame);
	  } else if (OpCodes.f2i==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeF2i(instr,inputFrame);
	  } else if (OpCodes.f2l==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeF2l(instr,inputFrame);
	  } else if (OpCodes.fadd==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFadd(instr,inputFrame);
	  } else if (OpCodes.faload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFaload(instr,inputFrame);
	  } else if (OpCodes.fastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFastore(instr,inputFrame);
	  } else if (OpCodes.fcmpg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFcmpg(instr,inputFrame);
	  } else if (OpCodes.fcmpl==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFcmpl(instr,inputFrame);
	  } else if (OpCodes.fconst_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFconst_0(instr,inputFrame);
	  } else if (OpCodes.fconst_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFconst_1(instr,inputFrame);
	  } else if (OpCodes.fconst_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFconst_2(instr,inputFrame);
	  } else if (OpCodes.fdiv==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFdiv(instr,inputFrame);
	  } else if (OpCodes.fload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFload(instr,inputFrame);
	  } else if (OpCodes.fload_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFload_0(instr,inputFrame);
	  } else if (OpCodes.fload_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFload_1(instr,inputFrame);
	  } else if (OpCodes.fload_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFload_2(instr,inputFrame);
	  } else if (OpCodes.fload_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFload_3(instr,inputFrame);
	  } else if (OpCodes.fmul==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFmul(instr,inputFrame);
	  } else if (OpCodes.fneg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFneg(instr,inputFrame);
	  } else if (OpCodes.frem==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFrem(instr,inputFrame);
	  } else if (OpCodes.freturn==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFreturn(instr,inputFrame);
	  } else if (OpCodes.fstore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFstore(instr,inputFrame);
	  } else if (OpCodes.fstore_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFstore_0(instr,inputFrame);
	  } else if (OpCodes.fstore_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFstore_1(instr,inputFrame);
	  } else if (OpCodes.fstore_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFstore_2(instr,inputFrame);
	  } else if (OpCodes.fstore_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFstore_3(instr,inputFrame);
	  } else if (OpCodes.fsub==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeFsub(instr,inputFrame);
	  } else if (OpCodes.getfield==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeGetfield(instr,inputFrame);
	  } else if (OpCodes.getstatic==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeGetstatic(instr,inputFrame);
	  } else if (OpCodes.goto_==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeGoto_(instr,inputFrame);
	  } else if (OpCodes.goto_w==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeGoto_w(instr,inputFrame);
	  } else if (OpCodes.i2b==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2b(instr,inputFrame);
	  } else if (OpCodes.i2c==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2c(instr,inputFrame);
	  } else if (OpCodes.i2d==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2d(instr,inputFrame);
	  } else if (OpCodes.i2f==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2f(instr,inputFrame);
	  } else if (OpCodes.i2l==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2l(instr,inputFrame);
	  } else if (OpCodes.i2s==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeI2s(instr,inputFrame);
	  } else if (OpCodes.iadd==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIadd(instr,inputFrame);
	  } else if (OpCodes.iaload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIaload(instr,inputFrame);
	  } else if (OpCodes.iand==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIand(instr,inputFrame);
	  } else if (OpCodes.iastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIastore(instr,inputFrame);
	  } else if (OpCodes.iconst_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_0(instr,inputFrame);
	  } else if (OpCodes.iconst_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_1(instr,inputFrame);
	  } else if (OpCodes.iconst_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_2(instr,inputFrame);
	  } else if (OpCodes.iconst_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_3(instr,inputFrame);
	  } else if (OpCodes.iconst_4==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_4(instr,inputFrame);
	  } else if (OpCodes.iconst_5==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_5(instr,inputFrame);
	  } else if (OpCodes.iconst_m1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIconst_m1(instr,inputFrame);
	  } else if (OpCodes.idiv==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIdiv(instr,inputFrame);
	  } else if (OpCodes.if_acmpeq==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_acmpeq(instr,inputFrame);
	  } else if (OpCodes.if_acmpne==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_acmpne(instr,inputFrame);
	  } else if (OpCodes.if_icmpeq==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmpeq(instr,inputFrame);
	  } else if (OpCodes.if_icmpge==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmpge(instr,inputFrame);
	  } else if (OpCodes.if_icmpgt==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmpgt(instr,inputFrame);
	  } else if (OpCodes.if_icmple==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmple(instr,inputFrame);
	  } else if (OpCodes.if_icmplt==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmplt(instr,inputFrame);
	  } else if (OpCodes.if_icmpne==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIf_icmpne(instr,inputFrame);
	  } else if (OpCodes.ifeq==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfeq(instr,inputFrame);
	  } else if (OpCodes.ifge==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfge(instr,inputFrame);
	  } else if (OpCodes.ifgt==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfgt(instr,inputFrame);
	  } else if (OpCodes.ifle==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfle(instr,inputFrame);
	  } else if (OpCodes.iflt==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIflt(instr,inputFrame);
	  } else if (OpCodes.ifne==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfne(instr,inputFrame);
	  } else if (OpCodes.ifnonnull==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfnonnull(instr,inputFrame);
	  } else if (OpCodes.ifnull==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIfnull(instr,inputFrame);
	  } else if (OpCodes.iinc==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIinc(instr,inputFrame);
	  } else if (OpCodes.iload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIload(instr,inputFrame);
	  } else if (OpCodes.iload_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIload_0(instr,inputFrame);
	  } else if (OpCodes.iload_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIload_1(instr,inputFrame);
	  } else if (OpCodes.iload_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIload_2(instr,inputFrame);
	  } else if (OpCodes.iload_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIload_3(instr,inputFrame);
	  } else if (OpCodes.imul==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeImul(instr,inputFrame);
	  } else if (OpCodes.ineg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIneg(instr,inputFrame);
	  } else if (OpCodes.instanceof_==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInstanceof_(instr,inputFrame);
	  } else if (OpCodes.invokedynamic==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInvokedynamic(instr,inputFrame);
	  } else if (OpCodes.invokeinterface==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInvokeinterface(instr,inputFrame);
	  } else if (OpCodes.invokespecial==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInvokespecial(instr,inputFrame);
	  } else if (OpCodes.invokestatic==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInvokestatic(instr,inputFrame);
	  } else if (OpCodes.invokevirtual==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeInvokevirtual(instr,inputFrame);
	  } else if (OpCodes.ior==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIor(instr,inputFrame);
	  } else if (OpCodes.irem==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIrem(instr,inputFrame);
	  } else if (OpCodes.ireturn==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIreturn(instr,inputFrame);
	  } else if (OpCodes.ishl==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIshl(instr,inputFrame);
	  } else if (OpCodes.ishr==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIshr(instr,inputFrame);
	  } else if (OpCodes.istore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIstore(instr,inputFrame);
	  } else if (OpCodes.istore_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIstore_0(instr,inputFrame);
	  } else if (OpCodes.istore_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIstore_1(instr,inputFrame);
	  } else if (OpCodes.istore_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIstore_2(instr,inputFrame);
	  } else if (OpCodes.istore_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIstore_3(instr,inputFrame);
	  } else if (OpCodes.isub==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIsub(instr,inputFrame);
	  } else if (OpCodes.iushr==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIushr(instr,inputFrame);
	  } else if (OpCodes.ixor==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeIxor(instr,inputFrame);
	  } else if (OpCodes.jsr==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeJsr(instr,inputFrame);
	  } else if (OpCodes.jsr_w==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeJsr_w(instr,inputFrame);
	  } else if (OpCodes.l2d==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeL2d(instr,inputFrame);
	  } else if (OpCodes.l2f==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeL2f(instr,inputFrame);
	  } else if (OpCodes.l2i==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeL2i(instr,inputFrame);
	  } else if (OpCodes.ladd==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLadd(instr,inputFrame);
	  } else if (OpCodes.laload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLaload(instr,inputFrame);
	  } else if (OpCodes.land==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLand(instr,inputFrame);
	  } else if (OpCodes.lastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLastore(instr,inputFrame);
	  } else if (OpCodes.lcmp==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLcmp(instr,inputFrame);
	  } else if (OpCodes.lconst_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLconst_0(instr,inputFrame);
	  } else if (OpCodes.lconst_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLconst_1(instr,inputFrame);
	  } else if (OpCodes.ldc==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLdc(instr,inputFrame);
	  } else if (OpCodes.ldc2_w==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLdc2_w(instr,inputFrame);
	  } else if (OpCodes.ldc_w==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLdc_w(instr,inputFrame);
	  } else if (OpCodes.ldiv==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLdiv(instr,inputFrame);
	  } else if (OpCodes.lload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLload(instr,inputFrame);
	  } else if (OpCodes.lload_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLload_0(instr,inputFrame);
	  } else if (OpCodes.lload_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLload_1(instr,inputFrame);
	  } else if (OpCodes.lload_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLload_2(instr,inputFrame);
	  } else if (OpCodes.lload_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLload_3(instr,inputFrame);
	  } else if (OpCodes.lmul==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLmul(instr,inputFrame);
	  } else if (OpCodes.lneg==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLneg(instr,inputFrame);
	  } else if (OpCodes.lookupswitch==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLookupswitch(instr,inputFrame);
	  } else if (OpCodes.lor==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLor(instr,inputFrame);
	  } else if (OpCodes.lrem==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLrem(instr,inputFrame);
	  } else if (OpCodes.lreturn==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLreturn(instr,inputFrame);
	  } else if (OpCodes.lshl==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLshl(instr,inputFrame);
	  } else if (OpCodes.lshr==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLshr(instr,inputFrame);
	  } else if (OpCodes.lstore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLstore(instr,inputFrame);
	  } else if (OpCodes.lstore_0==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLstore_0(instr,inputFrame);
	  } else if (OpCodes.lstore_1==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLstore_1(instr,inputFrame);
	  } else if (OpCodes.lstore_2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLstore_2(instr,inputFrame);
	  } else if (OpCodes.lstore_3==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLstore_3(instr,inputFrame);
	  } else if (OpCodes.lsub==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLsub(instr,inputFrame);
	  } else if (OpCodes.lushr==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLushr(instr,inputFrame);
	  } else if (OpCodes.lxor==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeLxor(instr,inputFrame);
	  } else if (OpCodes.monitorenter==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeMonitorenter(instr,inputFrame);
	  } else if (OpCodes.monitorexit==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeMonitorexit(instr,inputFrame);
	  } else if (OpCodes.multianewarray==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeMultianewarray(instr,inputFrame);
	  } else if (OpCodes.new_==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeNew_(instr,inputFrame);
	  } else if (OpCodes.newarray==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeNewarray(instr,inputFrame);
	  } else if (OpCodes.nop==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeNop(instr,inputFrame);
	  } else if (OpCodes.pop==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executePop(instr,inputFrame);
	  } else if (OpCodes.pop2==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executePop2(instr,inputFrame);
	  } else if (OpCodes.putfield==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executePutfield(instr,inputFrame);
	  } else if (OpCodes.putstatic==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executePutstatic(instr,inputFrame);
	  } else if (OpCodes.ret==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeRet(instr,inputFrame);
	  } else if (OpCodes.return_==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeReturn_(instr,inputFrame);
	  } else if (OpCodes.saload==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeSaload(instr,inputFrame);
	  } else if (OpCodes.sastore==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeSastore(instr,inputFrame);
	  } else if (OpCodes.sipush==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeSipush(instr,inputFrame);
	  } else if (OpCodes.swap==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeSwap(instr,inputFrame);
	  } else if (OpCodes.tableswitch==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeTableswitch(instr,inputFrame);
	  } else if (OpCodes.wide==instr.getOpCode()) {
	    inputFrame=inputFrame.copy();
	    return executeWide(instr,inputFrame);
	  } else {
	    throw new IllegalArgumentException("unknown op code: "+Integer.toHexString(instr.getOpCode()));
	  }

	}

	public Frame executeAaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.OBJECT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return new ObjectValueType(componentDescriptor, parent);
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "object";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isObject();
			}
		};
		
		return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeAastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.OBJECT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return new ObjectValueType(componentDescriptor, parent);
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "object";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isObject();
			}
		};
		
		return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeAconst_null(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.NULL);
	  return inputFrame;
	}

	public Frame executeAload(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.REFERENCE);
	}

	public Frame executeAload_0(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.REFERENCE);
	}

	public Frame executeAload_1(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.REFERENCE);
	}

	public Frame executeAload_2(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.REFERENCE);
	}

	public Frame executeAload_3(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.REFERENCE);
	}

	public Frame executeAnewarray(AbstractInstruction instr, Frame inputFrame) {
	  ClassInfo ci = (ClassInfo)((ConstantPoolInstruction)instr).getCpEntry();
	  TypeDescriptor td = ci.getDescriptor();
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.push(new ObjectValueType(new TypeDescriptor("["+td.getValue()), parent));
	  return inputFrame;
	}

	public Frame executeAreturn(AbstractInstruction instr, Frame inputFrame) {
	  TypeDescriptor returnType = parent.getMethod().getMethodDescriptor().getReturnType();
	  if (!returnType.isObject()) {
		  throw new VerifyException(instr.getIndex(), "instruction not allowed with return type "+returnType.getValue());
	  }
	  inputFrame.pop(new ObjectValueType(returnType, parent));
	  return inputFrame;
	}

	public Frame executeArraylength(AbstractInstruction instr, Frame inputFrame) {
	  ObjectValueType valueType = (ObjectValueType)inputFrame.pop(VerificationType.OBJECT);
	  if (!valueType.getDesc().isArray()) {
		  throw new VerifyException(instr.getIndex(), "expected an array on stack but got "+valueType.getDesc());
	  }
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeAstore(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.OBJECT);
	}

	public Frame executeAstore_0(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.OBJECT);
	}

	public Frame executeAstore_1(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.OBJECT);
	}

	public Frame executeAstore_2(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.OBJECT);
	}

	public Frame executeAstore_3(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.OBJECT);
	}

	public Frame executeAthrow(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.THROWABLE);
	  return inputFrame;
	}

	public Frame executeBaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "byte or boolean";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isByte() || componentDescriptor.isBoolean();
			}
		};
		
		return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeBastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "byte or boolean";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isByte() || componentDescriptor.isBoolean();
			}
		};
		
		return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeBipush(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeCaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "char";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isCharacter();
			}
		};
		
		return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeCastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "char";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isCharacter();
			}
		};
		
		return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeCheckcast(AbstractInstruction instr, Frame inputFrame) {
	  ClassInfo ci = (ClassInfo)((ConstantPoolInstruction)instr).getCpEntry();
	  inputFrame.pop(VerificationType.OBJECT);
	  inputFrame.push(new ObjectValueType(ci.getDescriptor(), parent));
	  return inputFrame;
	}

	public Frame executeD2f(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.DOUBLE, VerificationType.FLOAT, inputFrame);
	}

	public Frame executeD2i(AbstractInstruction instr, Frame inputFrame) {
		return executeConversion(VerificationType.DOUBLE, VerificationType.INT, inputFrame);
	}

	public Frame executeD2l(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.DOUBLE, VerificationType.LONG, inputFrame);
	}

	public Frame executeDadd(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.DOUBLE;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.DOUBLE;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "double";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isDouble();
			}
		};
		
		return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeDastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.DOUBLE;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.DOUBLE;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "double";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isDouble();
			}
		};
		
		return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeDcmpg(AbstractInstruction instr, Frame inputFrame) {
	  return executeCompare(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDcmpl(AbstractInstruction instr, Frame inputFrame) {
	  return executeCompare(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDconst_0(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.DOUBLE);
	  return inputFrame;
	}

	public Frame executeDconst_1(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.DOUBLE);
	  return inputFrame;
	}

	public Frame executeDdiv(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDload(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDload_0(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDload_1(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDload_2(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDload_3(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDmul(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDneg(AbstractInstruction instr, Frame inputFrame) {
	  return executeOneTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDrem(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDreturn(AbstractInstruction instr, Frame inputFrame) {
		inputFrame.pop(VerificationType.DOUBLE);
	  	return inputFrame;
	}

	public Frame executeDstore(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDstore_0(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDstore_1(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDstore_2(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDstore_3(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.DOUBLE);
	}

	public Frame executeDsub(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeDup(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType t = inputFrame.pop(VerificationType.ONE_WORD);
	  inputFrame.push(t);
	  inputFrame.push(t);
	  return inputFrame;
	}

	public Frame executeDup2(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType v1 = inputFrame.pop(VerificationType.TOP);
	  if (VerificationType.TWO_WORD.isAssignableFrom(v1)) {
		  inputFrame.push(v1);
		  inputFrame.push(v1);
	  } else {
		  VerificationType v2 = inputFrame.pop(VerificationType.ONE_WORD);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
	  }
	  return inputFrame;
	}

	public Frame executeDup2_x1(AbstractInstruction instr, Frame inputFrame) {
		VerificationType v1 = inputFrame.pop(VerificationType.TOP);
	    if (VerificationType.TWO_WORD.isAssignableFrom(v1)) {
	      VerificationType v2 = inputFrame.pop(VerificationType.ONE_WORD);
		  inputFrame.push(v1);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
	    } else {
		  VerificationType v2 = inputFrame.pop(VerificationType.ONE_WORD);
		  VerificationType v3 = inputFrame.pop(VerificationType.ONE_WORD);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
		  inputFrame.push(v3);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
	    }
	  return inputFrame;
	}

	public Frame executeDup2_x2(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType v1 = inputFrame.pop(VerificationType.TOP);
	  if (VerificationType.TWO_WORD.isAssignableFrom(v1)) {
		  VerificationType v2 = inputFrame.pop(VerificationType.TOP);
		  if (VerificationType.TWO_WORD.isAssignableFrom(v2)) {
			  inputFrame.push(v1);
			  inputFrame.push(v2);
			  inputFrame.push(v1);
		  } else {
			  VerificationType v3 = inputFrame.pop(VerificationType.ONE_WORD);
			  inputFrame.push(v1);
			  inputFrame.push(v3);
			  inputFrame.push(v2);
			  inputFrame.push(v1);
		  }
	  } else {
		  VerificationType v2 = inputFrame.pop(VerificationType.ONE_WORD);
		  VerificationType v3 = inputFrame.pop(VerificationType.TOP);
		  if (VerificationType.TWO_WORD.isAssignableFrom(v3)) {
			  inputFrame.push(v2);
			  inputFrame.push(v1);
			  inputFrame.push(v3);
			  inputFrame.push(v2);
			  inputFrame.push(v1);
		  } else {
			  VerificationType v4 = inputFrame.pop(VerificationType.ONE_WORD);
			  inputFrame.push(v2);
			  inputFrame.push(v1);
			  inputFrame.push(v4);
			  inputFrame.push(v3);
			  inputFrame.push(v2);
			  inputFrame.push(v1);
		  }
	  }
	  return inputFrame;
	}

	public Frame executeDup_x1(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType v1 = inputFrame.pop(VerificationType.ONE_WORD);
	  VerificationType v2 = inputFrame.pop(VerificationType.ONE_WORD);
	  inputFrame.push(v1);
	  inputFrame.push(v2);
	  inputFrame.push(v1);
	  return inputFrame;
	}

	public Frame executeDup_x2(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType v1 = inputFrame.pop(VerificationType.ONE_WORD);
	  VerificationType v2 = inputFrame.pop(VerificationType.TOP);
	  if (VerificationType.TWO_WORD.isAssignableFrom(v2)) {
		  inputFrame.push(v1);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
	  } else {
		  VerificationType v3 = inputFrame.pop(VerificationType.ONE_WORD);
		  inputFrame.push(v1);
		  inputFrame.push(v3);
		  inputFrame.push(v2);
		  inputFrame.push(v1);
	  }
	  return inputFrame;
	}

	public Frame executeF2d(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.FLOAT, VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeF2i(AbstractInstruction instr, Frame inputFrame) {
		return executeConversion(VerificationType.FLOAT, VerificationType.INT, inputFrame);
	}

	public Frame executeF2l(AbstractInstruction instr, Frame inputFrame) {
		return executeConversion(VerificationType.FLOAT, VerificationType.LONG, inputFrame);
	}

	public Frame executeFadd(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFaload(AbstractInstruction instr, Frame inputFrame) {
	  ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.FLOAT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.FLOAT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "float";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isFloat();
			}
		};
	  return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeFastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.FLOAT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.FLOAT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "float";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isFloat();
			}
		};
	  return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeFcmpg(AbstractInstruction instr, Frame inputFrame) {
	  return executeCompare(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFcmpl(AbstractInstruction instr, Frame inputFrame) {
	  return executeCompare(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFconst_0(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.FLOAT);
	  return inputFrame;
	}

	public Frame executeFconst_1(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.FLOAT);
	  return inputFrame;
	}

	public Frame executeFconst_2(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.FLOAT);
	  return inputFrame;
	}

	public Frame executeFdiv(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFload(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFload_0(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFload_1(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFload_2(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFload_3(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFmul(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFneg(AbstractInstruction instr, Frame inputFrame) {
	  return executeOneTier(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFrem(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.FLOAT, inputFrame);
	}

	public Frame executeFreturn(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.FLOAT);
	  return inputFrame;
	}

	public Frame executeFstore(AbstractInstruction instr, Frame inputFrame) {
	   return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFstore_0(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFstore_1(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFstore_2(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFstore_3(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeFsub(AbstractInstruction instr, Frame inputFrame) {
	   return executeStore(instr, inputFrame, VerificationType.FLOAT);
	}

	public Frame executeGetfield(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  FieldInfo fi = (FieldInfo)cpi.getInfo();
	  if (fi.getModifier().isStatic()) {
		  throw new IllegalStateException("illegal state - getfield on static field!");
	  }
	  ExternalClassInfo cl = fi.getParent();
	  if (cl.getDescriptor().isArray()) {
		  throw new IllegalStateException("illegal state - expected class or interface but got array!");
	  }
	  inputFrame.pop(new ObjectValueType(cl.getDescriptor(), parent));
	  inputFrame.push(VerificationType.createTypeFromDescriptor(fi.getDescriptor(), parent));
	  return inputFrame;
	}

	public Frame executeGetstatic(AbstractInstruction instr, Frame inputFrame) {
	   ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	   FieldInfo fi = (FieldInfo)cpi.getInfo();
	   if (!fi.getModifier().isStatic()) {
		  throw new IllegalStateException("illegal state - getstatic on static field!");
	   }
	   ExternalClassInfo cl = fi.getParent();
	   if (cl.getDescriptor().isArray()) {
		  throw new IllegalStateException("illegal state - expected class or interface but got array!");
	   }
	   inputFrame.push(VerificationType.createTypeFromDescriptor(fi.getDescriptor(), parent));
	   return inputFrame;
	}

	public Frame executeGoto_(AbstractInstruction instr, Frame inputFrame) {
	  return inputFrame;
	}

	public Frame executeGoto_w(AbstractInstruction instr, Frame inputFrame) {
	  return inputFrame;
	}

	public Frame executeI2b(AbstractInstruction instr, Frame inputFrame) {
	  return executeOneTier(VerificationType.INT, inputFrame);
	}

	public Frame executeI2c(AbstractInstruction instr, Frame inputFrame) {
	  return executeOneTier(VerificationType.INT, inputFrame);
	}

	public Frame executeI2d(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.INT, VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeI2f(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.INT, VerificationType.FLOAT, inputFrame);
	}

	public Frame executeI2l(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.INT, VerificationType.LONG, inputFrame);
	}

	public Frame executeI2s(AbstractInstruction instr, Frame inputFrame) {
		return executeOneTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIadd(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "int";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isInteger();
			}
		};
	  return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeIand(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "int";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isInteger();
			}
		};
	    return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeIconst_0(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_1(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_2(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_3(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_4(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_5(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIconst_m1(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIdiv(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIf_acmpeq(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeIf_acmpne(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeIf_icmpeq(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIf_icmpge(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIf_icmpgt(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIf_icmple(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIf_icmplt(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIf_icmpne(AbstractInstruction instr, Frame inputFrame) {
      inputFrame.pop(VerificationType.INT);
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfeq(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfge(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfgt(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfle(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIflt(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfne(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIfnonnull(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeIfnull(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeIinc(AbstractInstruction instr, Frame inputFrame) {
	  IincInstruction iinc = (IincInstruction)instr;
	  int index = iinc.getRegisterIndex();
	  inputFrame.checkRegister(index, VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIload(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIload_0(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIload_1(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIload_2(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIload_3(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeImul(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIneg(AbstractInstruction instr, Frame inputFrame) {
	  return executeOneTier(VerificationType.INT, inputFrame);
	}

	public Frame executeInstanceof_(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.OBJECT);
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeInvokedynamic(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  InvokeDynamicInfo idi = (InvokeDynamicInfo)cpi.getCpEntry();
	  MethodDescriptor md = idi.getNameAndType().getMethodDescriptor();
	  invokeMethod(null, md, inputFrame);
	  return inputFrame;
	}

	public Frame executeInvokeinterface(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  MethodrefInfo mi = (MethodrefInfo)cpi.getCpEntry();
	  invokeMethod(mi.getClassReference().getDescriptor(), mi.getNameAndTypeReference().getMethodDescriptor(), inputFrame);
	  return inputFrame;
	}

	public Frame executeInvokespecial(AbstractInstruction instr, Frame inputFrame) {
	  String currentMethodName = parent.getMethod().getName().getValue();
	  String currentClassName = parent.getClazz().getThisClass().getClassName();
	  String superClassName = "";
	  if (parent.getClazz().getSuperClass() != null) {
		  superClassName = parent.getClazz().getSuperClass().getClassName();
	  }
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  MethodrefInfo mi = (MethodrefInfo)cpi.getCpEntry();
	  if (mi.getNameAndTypeReference().getName().equals("<init>")) {
		  String methodClassName = mi.getClassReference().getClassName();
		  MethodDescriptor method = mi.getNameAndTypeReference().getMethodDescriptor();
		  List<VerificationType> params = new ArrayList<VerificationType>();
		  if (!method.isVoid()) {
			  throw new IllegalStateException("non-void constructor");
		  }
		  for (TypeDescriptor td: method.getParameters()) {
			 params.add(VerificationType.createTypeFromDescriptor(td, parent));
		  }
		  for (int i=0;i<params.size(); i++) {
			  inputFrame.pop(params.get(params.size()-i-1));
		  }
		  VerificationType this_ = inputFrame.pop(VerificationType.UNINITIALIZED);
		  if (!currentMethodName.equals("<init>") && this_.equals(VerificationType.UNINITIALIZED_THIS)) {
			  throw new IllegalStateException("uninitialized_this in a non-constructor method");
		  }
		  if (this_ instanceof UninitializedValueType) {
			  TypeDescriptor desc = ((UninitializedValueType)this_).getDesc();
			  if (!desc.isObject()) {
				  throw new IllegalStateException("not object");
			  }
			  if (!desc.getClassName().equals(methodClassName)) {
				  throw new VerifyException(instr.getIndex(), "wrong constructor call");
			  }
			  inputFrame.replaceAllOccurences(this_, new ObjectValueType(desc, parent));
		  } else {
			  if (methodClassName.equals(currentClassName) || methodClassName.equals(superClassName)) {
				  inputFrame.replaceAllOccurences(this_, new ObjectValueType(new TypeDescriptor("L"+currentClassName+";"), parent));
			  } else {
				  throw new VerifyException(instr.getIndex(), "wrong constructor call");
			  }
		  }
		  
	  } else {
		  invokeMethod(mi.getClassReference().getDescriptor(), mi.getNameAndTypeReference().getMethodDescriptor(), inputFrame);
	  }
	  return inputFrame;
	}

	public Frame executeInvokestatic(AbstractInstruction instr, Frame inputFrame) {
		ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
		MethodrefInfo mi = (MethodrefInfo)cpi.getCpEntry();
		invokeMethod(null, mi.getNameAndTypeReference().getMethodDescriptor(), inputFrame);
		return inputFrame;
	}

	public Frame executeInvokevirtual(AbstractInstruction instr, Frame inputFrame) {
		ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
		MethodrefInfo mi = (MethodrefInfo)cpi.getCpEntry();
		invokeMethod(mi.getClassReference().getDescriptor(), mi.getNameAndTypeReference().getMethodDescriptor(), inputFrame);
		return inputFrame;
	}
	
	private void invokeMethod(TypeDescriptor this_, MethodDescriptor method, Frame inputFrame) {
		List<VerificationType> params = new ArrayList<VerificationType>();
		if (this_ != null) {
			params.add(VerificationType.createTypeFromDescriptor(this_, parent));
		}
		for (TypeDescriptor td: method.getParameters()) {
			params.add(VerificationType.createTypeFromDescriptor(td, parent));
		}
		VerificationType rt = null;
		if (!method.isVoid()) {
			rt = VerificationType.createTypeFromDescriptor(method.getReturnType(), parent);
		}
		for (int i=0;i<params.size(); i++) {
			inputFrame.pop(params.get(params.size()-i-1));
		}
		if (rt != null) {
			inputFrame.push(rt);
		}
	}

	public Frame executeIor(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIrem(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIreturn(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeIshl(AbstractInstruction instr, Frame inputFrame) {
	   return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIshr(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIstore(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIstore_0(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIstore_1(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIstore_2(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIstore_3(AbstractInstruction instr, Frame inputFrame) {
		return executeStore(instr, inputFrame, VerificationType.INT);
	}

	public Frame executeIsub(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIushr(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeIxor(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.INT, inputFrame);
	}

	public Frame executeJsr(AbstractInstruction instr, Frame inputFrame) {
	  throw new IllegalStateException("jsr not allowed!");
	}

	public Frame executeJsr_w(AbstractInstruction instr, Frame inputFrame) {
	  throw new IllegalStateException("jsr not allowed!");
	}

	public Frame executeL2d(AbstractInstruction instr, Frame inputFrame) {
	  return executeConversion(VerificationType.LONG, VerificationType.DOUBLE, inputFrame);
	}

	public Frame executeL2f(AbstractInstruction instr, Frame inputFrame) {
		return executeConversion(VerificationType.LONG, VerificationType.FLOAT, inputFrame);
	}

	public Frame executeL2i(AbstractInstruction instr, Frame inputFrame) {
		return executeConversion(VerificationType.LONG, VerificationType.INT, inputFrame);
	}

	public Frame executeLadd(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.LONG;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.LONG;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "long";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isLong();
			}
		};
	  return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeLand(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.LONG;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.LONG;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "long";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isLong();
			}
		};
	  return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeLcmp(AbstractInstruction instr, Frame inputFrame) {
	  return executeCompare(VerificationType.LONG, inputFrame);
	}

	public Frame executeLconst_0(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.LONG);
	  return inputFrame;
	}

	public Frame executeLconst_1(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.LONG);
	  return inputFrame;
	}

	public Frame executeLdc(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  AbstractConstantPoolEntry entry = cpi.getCpEntry();
	  if (entry instanceof IntegerInfo) {
		  inputFrame.push(VerificationType.INT);
	  } else if (entry instanceof FloatInfo) {
		  inputFrame.push(VerificationType.FLOAT);
	  } else if (entry instanceof ClassInfo) {
		  inputFrame.push(VerificationType.CLASS);
	  } else if (entry instanceof MethodTypeInfo) {
		  inputFrame.push(VerificationType.METHOD_TYPE);
	  } else if (entry instanceof MethodHandleInfo) {
		  inputFrame.push(VerificationType.METHOD_HANDLE);
	  } else {
		  throw new IllegalStateException("Unexpected cost: "+entry.getClass().getName());
	  }
	  return inputFrame;
	}

	public Frame executeLdc2_w(AbstractInstruction instr, Frame inputFrame) {
		ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
		AbstractConstantPoolEntry entry = cpi.getCpEntry();
		if (entry instanceof DoubleInfo) {
		  inputFrame.push(VerificationType.DOUBLE);
		} else if (entry instanceof LongInfo) {
		  inputFrame.push(VerificationType.LONG);
		} else {
		  throw new IllegalStateException("Unexpected cost: "+entry.getClass().getName());
		}
		return inputFrame;
	}

	public Frame executeLdc_w(AbstractInstruction instr, Frame inputFrame) {
	  return executeLdc(instr, inputFrame);
	}

	public Frame executeLdiv(AbstractInstruction instr, Frame inputFrame) {
	  return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLload(AbstractInstruction instr, Frame inputFrame) {
	  return executeLoad(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLload_0(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLload_1(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLload_2(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLload_3(AbstractInstruction instr, Frame inputFrame) {
		return executeLoad(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLmul(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLneg(AbstractInstruction instr, Frame inputFrame) {
		return executeOneTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLookupswitch(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeLor(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLrem(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLreturn(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.LONG);
	  return inputFrame;
	}

	public Frame executeLshl(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLshr(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLstore(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLstore_0(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLstore_1(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLstore_2(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLstore_3(AbstractInstruction instr, Frame inputFrame) {
	  return executeStore(instr, inputFrame, VerificationType.LONG);
	}

	public Frame executeLsub(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLushr(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeLxor(AbstractInstruction instr, Frame inputFrame) {
		return executeTwoTier(VerificationType.LONG, inputFrame);
	}

	public Frame executeMonitorenter(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeMonitorexit(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.REFERENCE);
	  return inputFrame;
	}

	public Frame executeMultianewarray(AbstractInstruction instr, Frame inputFrame) {
	  MultianewarrayInstruction mi = (MultianewarrayInstruction)instr;
	  ClassInfo cli = (ClassInfo)mi.getCpEntry();
	  int dim = mi.getDimensions();
	  if (!cli.getDescriptor().isArray() || cli.getDescriptor().getArrayDimension()<dim) {
		  throw new IllegalStateException("illegal arguments");
	  }
	  ObjectValueType type = new ObjectValueType(cli.getDescriptor(), parent);
	  for (int i=0;i<dim; i++) {
		  inputFrame.pop(VerificationType.INT);
	  }
	  inputFrame.push(type);
	  return inputFrame;
	}

	public Frame executeNew_(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  ExternalClassInfo eci = (ExternalClassInfo)cpi.getInfo();
	  if (eci.isArray() || eci.getModifier().isInterface()) {
		  throw new IllegalStateException("expected class as argument");
	  }
	  UninitializedValueType uvt = new UninitializedValueType(eci.getDescriptor(), instr.getIndex());
	  if (inputFrame.isOnStack(uvt)) {
		  throw new VerifyException(instr.getIndex(), "uninitialized value already on the stack");
	  } else {
		  inputFrame.replaceAllRegisterOccurences(uvt, VerificationType.TOP);
		  inputFrame.push(uvt);
	  }
	  return inputFrame;
	}

	public Frame executeNewarray(AbstractInstruction instr, Frame inputFrame) {
	  NewarrayInstruction nei = (NewarrayInstruction)instr;
	  inputFrame.pop(VerificationType.INT);
	  inputFrame.push(VerificationType.createTypeFromDescriptor(nei.getDesc(), parent));
	  return inputFrame;
	}

	public Frame executeNop(AbstractInstruction instr, Frame inputFrame) {
	  return inputFrame;
	}

	public Frame executePop(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.ONE_WORD);
	  return inputFrame;
	}

	public Frame executePop2(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.TWO_WORD);
	  return inputFrame;
	}

	public Frame executePutfield(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  FieldInfo fi = (FieldInfo)cpi.getInfo();
	  if (fi.getModifier().isStatic()) {
	     throw new IllegalStateException("illegal state - putfield on static field!");
	  }
	  ExternalClassInfo cl = fi.getParent();
	  if (cl.getDescriptor().isArray()) {
		 throw new IllegalStateException("illegal state - expected class or interface but got array!");
	  }
	  
	  inputFrame.pop(new ObjectValueType(fi.getDescriptor(), parent));
	  VerificationType top = inputFrame.peek(VerificationType.REFERENCE);
	  if (top.equals(VerificationType.UNINITIALIZED_THIS)) {
		  TypeDescriptor expected = new TypeDescriptor("L"+parent.getClazz().getThisClass().getClassName()+";");
		  if (!expected.equals(cl.getDescriptor())) {
			  throw new VerifyException(instr.getIndex(), "illegal field access");
		  }
		  inputFrame.pop(VerificationType.UNINITIALIZED_THIS);
	  } else  {
		  inputFrame.pop(new ObjectValueType(cl.getDescriptor(), parent));
	  }
	  return inputFrame;
	}

	public Frame executePutstatic(AbstractInstruction instr, Frame inputFrame) {
	  ConstantPoolInstruction cpi = (ConstantPoolInstruction)instr;
	  FieldInfo fi = (FieldInfo)cpi.getInfo();
	  if (fi.getModifier().isStatic()) {
			  throw new IllegalStateException("illegal state - putstatic on non-static field!");
	  }
	  ExternalClassInfo cl = fi.getParent();
	  if (cl.getDescriptor().isArray()) {
		  throw new IllegalStateException("illegal state - expected class or interface but got array!");
	  }
	  inputFrame.pop(VerificationType.createTypeFromDescriptor(fi.getDescriptor(), parent));
	  return inputFrame;
	}

	public Frame executeRet(AbstractInstruction instr, Frame inputFrame) {
	  throw new IllegalStateException("ret isn't allowed!");
	}

	public Frame executeReturn_(AbstractInstruction instr, Frame inputFrame) {
	  return inputFrame;
	}

	public Frame executeSaload(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "short";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isShort();
			}
		};
		return executeArrayLoad(instr, inputFrame, arrayHandler);
	}

	public Frame executeSastore(AbstractInstruction instr, Frame inputFrame) {
		ArrayHandler arrayHandler = new ArrayHandler() {
			
			@Override
			public VerificationType getComponentVerificationType() {
				return VerificationType.INT;
			}
			
			@Override
			public VerificationType getComponentVerificationType(
					TypeDescriptor componentDescriptor) {
				return VerificationType.INT;
			}
			
			@Override
			public String getComponentTypeLabel() {
				return "short";
			}
			
			@Override
			public boolean checkComponentType(TypeDescriptor componentDescriptor) {
				return componentDescriptor.isShort();
			}
		};
		return executeArrayStore(instr, inputFrame, arrayHandler);
	}

	public Frame executeSipush(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.push(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeSwap(AbstractInstruction instr, Frame inputFrame) {
	  VerificationType v1 = inputFrame.pop(VerificationType.TOP);
	  VerificationType v2 = inputFrame.pop(VerificationType.TOP);
	  inputFrame.push(v1);
	  inputFrame.push(v2);
	  return inputFrame;
	}

	public Frame executeTableswitch(AbstractInstruction instr, Frame inputFrame) {
	  inputFrame.pop(VerificationType.INT);
	  return inputFrame;
	}

	public Frame executeWide(AbstractInstruction instr, Frame inputFrame) {
	  throw new IllegalStateException("Schpuldn't be called!");
	}

	private Frame executeLoad(AbstractInstruction instr, Frame inputFrame, VerificationType expectedType) {
		int index = ((IRegisterIndexInstruction)instr).getRegisterIndex();
		inputFrame.load(expectedType, index);
		return inputFrame;
	}
	
	private Frame executeStore(AbstractInstruction instr, Frame inputFrame, VerificationType expectedType) {
		int index = ((IRegisterIndexInstruction)instr).getRegisterIndex();
		inputFrame.store(expectedType, index);
		return inputFrame;
	}
	
	private Frame executeConversion(VerificationType from, VerificationType to, Frame inputFrame) {
		inputFrame.pop(from);
		inputFrame.push(to);
		return inputFrame;
	}
	
	private Frame executeTwoTier(VerificationType t, Frame inputFrame) {
		inputFrame.pop(t);
		inputFrame.pop(t);
		inputFrame.push(t);
		return inputFrame;
	}
	
	private Frame executeOneTier(VerificationType t, Frame inputFrame) {
		inputFrame.pop(t);
		inputFrame.push(t);
		return inputFrame;
	}
	
	private Frame executeCompare(VerificationType t, Frame inputFrame) {
		inputFrame.pop(t);
		inputFrame.pop(t);
		inputFrame.push(VerificationType.INT);
		return inputFrame;
	}
	
	
	public Frame executeArrayLoad(AbstractInstruction instr, Frame inputFrame, ArrayHandler arrayHandler) {
		  inputFrame.pop(VerificationType.INT);
		  ObjectValueType valueType = (ObjectValueType)inputFrame.pop(VerificationType.OBJECT);
		  if (valueType.getDesc().isArray()) {
			  TypeDescriptor componentType = valueType.getDesc().getComponentType();
			  if (arrayHandler.checkComponentType(componentType)) {
				  inputFrame.push(arrayHandler.getComponentVerificationType(componentType));
			  } else {
				  throw new VerifyException(instr.getIndex(), "expected an array of "+arrayHandler.getComponentTypeLabel()+" on the stack but got "+valueType.getDesc());
			  }
		  } else {
			  throw new VerifyException(instr.getIndex(), "expected an array of "+arrayHandler.getComponentTypeLabel()+" on the stack but got "+valueType.getDesc());
		  }
		  return inputFrame;
	}
	
	public Frame executeArrayStore(AbstractInstruction instr, Frame inputFrame, ArrayHandler arrayHandler) {
		VerificationType t = inputFrame.pop(arrayHandler.getComponentVerificationType());
		inputFrame.pop(VerificationType.INT);
		ObjectValueType valueType = (ObjectValueType)inputFrame.pop(VerificationType.OBJECT);
		if (valueType.getDesc().isArray()) {
		  TypeDescriptor componentType = valueType.getDesc().getComponentType();
		  if (arrayHandler.checkComponentType(componentType)) {
			  if (componentType.isObject()) {
				  ObjectValueType t2 = new ObjectValueType(componentType, parent);
				  if (!t2.isAssignableFrom(t)) {
					  throw new VerifyException(instr.getIndex(), "expected a "+arrayHandler.getComponentTypeLabel()+" on the stack but got "+((ObjectValueType)t).getDesc());
				  }
			  }
		  } else {
			  throw new VerifyException(instr.getIndex(), "expected an array of "+arrayHandler.getComponentTypeLabel()+" on the stack but got "+valueType.getDesc());
		  }
		} else {
			throw new VerifyException(instr.getIndex(), "expected an array of "+arrayHandler.getComponentTypeLabel()+" on the stack but got "+valueType.getDesc());
		}
		return inputFrame;
	}
	
	
	
	public static void main(String [] args) {
		List<String> names = new ArrayList<String>();
		names.addAll(OpCodes.getVariableNames());
		Collections.sort(names);
		//Central method
		int currentIndent = 0;
		
		print(currentIndent,"public Frame execute(AbstractInstruction instr, Frame inputFrame) {");
		printempty();
		currentIndent = indent(currentIndent, false);
		int index = 0;
		for (String name: names) {
			if (index == 0) {
				print(currentIndent,"if (OpCodes."+name+"==instr.getOpCode()) {");
			} else {
				print(currentIndent,"} else if (OpCodes."+name+"==instr.getOpCode()) {");
			}
			
			currentIndent = indent(currentIndent, false);
			String methodName = "execute"+name.substring(0, 1).toUpperCase()+name.substring(1, name.length());
			print(currentIndent,"inputFrame=inputFrame.copy();");
			print(currentIndent,"return "+methodName+"(instr,inputFrame);");
			currentIndent = indent(currentIndent, true);
			index++;
		}
		print(currentIndent,"} else {");
		currentIndent = indent(currentIndent, false);
		print(currentIndent, "throw new IllegalArgumentException(\"unknown op code: \"+Integer.toHexString(instr.getOpCode()));");
		currentIndent = indent(currentIndent, true);
		print(currentIndent,"}");
		currentIndent = indent(currentIndent, true);
		printempty();
		print(currentIndent,"}");
		for (String name: names) {
			printempty();
			String methodName = "execute"+name.substring(0, 1).toUpperCase()+name.substring(1, name.length());
			print(currentIndent,"public Frame "+methodName+"(AbstractInstruction instr, Frame inputFrame) {");
			currentIndent = indent(currentIndent, false);
			print(currentIndent,"//TODO");
			print(currentIndent,"return inputFrameA;");
			currentIndent = indent(currentIndent, true);
			print(currentIndent,"}");
		}
		
		
		
		
	}
	
	public static int indent(int currentIndent, boolean back) {
		if (back) {
			currentIndent = currentIndent-2;
		} else {
			currentIndent = currentIndent+2;
		}
		
		return currentIndent;
	}
	
	public static void print(int currentIndent, String line) {
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<currentIndent; i++) {
			buf.append(' ');
		}
		buf.append(line);
		System.out.println(buf.toString());
	}
	
	public static void printempty() {
		System.out.println("");
	}

}

interface ArrayHandler {
	public boolean checkComponentType(TypeDescriptor componentDescriptor);
	public VerificationType getComponentVerificationType(TypeDescriptor componentDescriptor);
	public VerificationType getComponentVerificationType();
	public String getComponentTypeLabel();
}

