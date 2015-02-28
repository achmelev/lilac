package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;





































import org.jasm.item.IErrorEmitter;
import org.jasm.item.attribute.AbstractStackmapFrame;
import org.jasm.item.attribute.AbstractStackmapVariableinfo;
import org.jasm.item.attribute.AppendStackmapFrame;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.ChopStackmapFrame;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.DoubleStackmapVariableinfo;
import org.jasm.item.attribute.ExceptionHandler;
import org.jasm.item.attribute.ExceptionHandlerTable;
import org.jasm.item.attribute.FloatStackmapVariableinfo;
import org.jasm.item.attribute.FullStackmapFrame;
import org.jasm.item.attribute.IntegerStackmapVariableinfo;
import org.jasm.item.attribute.LongStackmapVariableinfo;
import org.jasm.item.attribute.NullStackmapVariableinfo;
import org.jasm.item.attribute.ObjectStackmapVariableinfo;
import org.jasm.item.attribute.SameExtendedStackmapFrame;
import org.jasm.item.attribute.SameLocalsOneStackitemExtendedStackmapFrame;
import org.jasm.item.attribute.SameLocalsOneStackitemStackmapFrame;
import org.jasm.item.attribute.SameStackmapFrame;
import org.jasm.item.attribute.StackMapAttributeContent;
import org.jasm.item.attribute.TopStackmapVariableinfo;
import org.jasm.item.attribute.UninitializedStackmapVariableinfo;
import org.jasm.item.attribute.UninitializedThisStackmapVariableinfo;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.AbstractSwitchInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.verify.error.BadCodeException;
import org.jasm.item.instructions.verify.error.FallOffException;
import org.jasm.item.instructions.verify.error.LinearMethodException;
import org.jasm.item.instructions.verify.error.MissingStackmapException;
import org.jasm.item.instructions.verify.error.MissingStackmapsDeclaration;
import org.jasm.item.instructions.verify.error.UnknownClassException;
import org.jasm.item.instructions.verify.types.IClassQuery;
import org.jasm.item.instructions.verify.types.ObjectValueType;
import org.jasm.item.instructions.verify.types.UninitializedValueType;
import org.jasm.item.instructions.verify.types.VerificationType;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;
import org.jasm.type.verifier.VerifierParams;

public class Verifier implements IClassQuery {
	
	private Instructions parent;
	
	private Clazz clazz;
	private Method method;
	private CodeAttributeContent code;
	private Interpreter interpeter;
	
	
	private List<Set<Integer>> followers = new ArrayList<Set<Integer>>();
	private List<Set<ExceptionHandler>> exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
	private Set<Integer> branchTargets = new HashSet<Integer>();
	private Set<Integer> exceptionTargets = new HashSet<Integer>();
	private Map<Integer, Frame> stackMapFrames = new HashMap<Integer, Frame>(); 
	
	private int currentInstructionIndex = -1;
	
	private boolean hasStackMapDeclaration;
	
	private Frame initialFrame = null;
	
	public void setParent(Instructions parent) {
		this.parent = parent;
		this.clazz = parent.getAncestor(Clazz.class);
		this.method = parent.getAncestor(Method.class);
		this.code = parent.getAncestor(CodeAttributeContent.class);
		this.interpeter = new Interpreter();
		this.interpeter.setParent(this);
	}
	
	private AbstractInstruction getInstructionAt(int index) {
		return parent.get(index);
	}
	
	private Set<Integer> getAllFollowersFor(int index) {
		Set<Integer> result = new HashSet<Integer>();
		result.addAll(followers.get(index));
		for (ExceptionHandler handler: exceptionHandlers.get(index)) {
			result.add(handler.getHandlerInstruction().getIndex());
		}
		
		return result;
	}
	
	private Set<Integer> getAllReachable(int index) {
		Set<Integer> result = new HashSet<Integer>();
		getAllReachable(index, result);
		return result;
	}

	
	private void getAllReachable(int index, Set<Integer> result) {
		List<Integer> unvisited = new ArrayList<Integer>();
		unvisited.add(index);
		while (unvisited.size() > 0) {
			Integer i = unvisited.remove((int)0);
			result.add(i);
			for (Integer f: getAllFollowersFor(i)) {
				if (!unvisited.contains(f) && !result.contains(f)) {
					unvisited.add(f);
				}
			}
		}
		
	}
	
	public void verify(VerifierParams params) {
		try {
			double version = clazz.getDecimalVersion().doubleValue();
			calculateFollowers();
			try {
				checkForBadCode();
			} catch (BadCodeException e) {
				if (version>50) {
					throw e;
				} else {
					return;
				}
			}
			checkAllReachable();
			createInitialFrame();
			if (version>=50) {
				try {
					doTypeChecking();
				} catch (VerifyException e) {
					if (version>50) {
						throw e;
					}
				} catch (LinearMethodException e) {
					throw e.getCause();
				}
			} else {
				
			}
		} catch (VerifyException e) {
			emitCodeVerifyError(e);
		} catch (RuntimeException e) {
			emitRuntimeError(e);
		}
		
		
	}
	
	private void createInitialFrame() {
		String className = clazz.getThisClass().getClassNameReference().getValue();
		boolean isConstructor = method.getName().getValue().equals("<init>");
		boolean isStatic = method.getModifier().isStatic();
		MethodDescriptor desc = method.getMethodDescriptor();
		int maxLocals = code.getMaxLocals();
		int maxStack = code.getMaxStack();
		if (maxStack < 0) {
			maxStack = Integer.MAX_VALUE;
		}
		
		initialFrame = Frame.createInitialFrame(className, isConstructor, isStatic, maxLocals, maxStack, desc, this);
	}
	
	private void doTypeChecking() {
		List<Attribute> attrs = code.getAttributes().getAttributesByContentType(StackMapAttributeContent.class);
		if (attrs.size() == 0 && !(branchTargets.isEmpty() && exceptionTargets.isEmpty())) {
			throw new MissingStackmapsDeclaration();
		}
		if (attrs.size() == 2) {
			throw new IllegalStateException("multiple stackmaps declared!");
		}
		hasStackMapDeclaration = (attrs.size() == 1);
		if (hasStackMapDeclaration) {
			createStackmapFrames((StackMapAttributeContent)attrs.get(0).getContent());
		}
		for (Integer b: branchTargets) {
			if (!stackMapFrames.containsKey(b)) {
				throw new MissingStackmapException(b);
			}
		}
		for (Integer b: exceptionTargets) {
			if (!stackMapFrames.containsKey(b)) {
				throw new MissingStackmapException(b);
			}
		}
		
		if (branchTargets.size() == 0 && 
				exceptionTargets.size() == 0 &&
				stackMapFrames.size() == 0) {
			checkLinearMethod();
		}
	}
	
	private void checkLinearMethod() {
		
		try {
			Frame currentFrame = initialFrame.copy();
			for (int i=0;i<parent.getSize(); i++) {
				currentInstructionIndex = i;
				AbstractInstruction instr = parent.get(currentInstructionIndex);
				if (instr instanceof BranchInstruction) {
					throw new IllegalStateException("Branch in a linear method");
				}
				if (instr instanceof AbstractSwitchInstruction)  {
					throw new IllegalStateException("Switch in a linear method");
				}
				if (instr instanceof ArgumentLessInstruction) {
					ArgumentLessInstruction ai = (ArgumentLessInstruction)instr;
					if (ai.isReturn() && currentInstructionIndex <(parent.getSize()-1)) {
						throw new IllegalStateException("return in the middle of a linear method");
					}
				}
				interpeter.execute(instr, currentFrame);
				
			}
		} catch (VerifyException e) {
			//e.printStackTrace();
			throw new LinearMethodException(e);
		}
	}
	
	private void createStackmapFrames(StackMapAttributeContent stackmapContent) {
		Frame previousFrame = initialFrame;
		for (int i=0;i<stackmapContent.getSize(); i++) {
			AbstractStackmapFrame stackMapFrame = stackmapContent.get(i);
			int index = stackMapFrame.getInstruction().getIndex();
			currentInstructionIndex = index;
			if (stackMapFrames.containsKey(index)) {
				throw new IllegalStateException("index "+index+" already has a stack frame!");
			}
			Frame fr = createFrameFromStackFrame(previousFrame, stackMapFrame);
			stackMapFrames.put(index, fr);
			previousFrame = fr;getClass();
			currentInstructionIndex = -1;
		}
		
	}
	
	private Frame createFrameFromStackFrame(Frame previousFrame, AbstractStackmapFrame stackMapFrame) {
		if (stackMapFrame instanceof SameStackmapFrame) {
			return previousFrame.copy();
		} else if (stackMapFrame instanceof SameExtendedStackmapFrame) {
			return previousFrame.copy();
		} else if (stackMapFrame instanceof SameLocalsOneStackitemStackmapFrame) {
			SameLocalsOneStackitemStackmapFrame slo = (SameLocalsOneStackitemStackmapFrame)stackMapFrame;
			return previousFrame.applyStackmapSameLocalsOneStackItem(createVerificationTypeFromStackMap(slo.getStackitemInfo()));
		} else if (stackMapFrame instanceof SameLocalsOneStackitemExtendedStackmapFrame) {
			SameLocalsOneStackitemExtendedStackmapFrame slo = (SameLocalsOneStackitemExtendedStackmapFrame)stackMapFrame;
			return previousFrame.applyStackmapSameLocalsOneStackItem(createVerificationTypeFromStackMap(slo.getStackitemInfo()));
		} else if (stackMapFrame instanceof ChopStackmapFrame) {
			ChopStackmapFrame slo = (ChopStackmapFrame)stackMapFrame;
			return previousFrame.applyStackmapChop(slo.getK());
		} else if (stackMapFrame instanceof AppendStackmapFrame) {
			AppendStackmapFrame slo = (AppendStackmapFrame)stackMapFrame;
			List<VerificationType> locals = new ArrayList<VerificationType>();
			for (AbstractStackmapVariableinfo info: slo.getLocals()) {
				locals.add(createVerificationTypeFromStackMap(info));
			}
			return previousFrame.applyStackmapAppend(locals);
		} else if (stackMapFrame instanceof FullStackmapFrame) {
			FullStackmapFrame slo = (FullStackmapFrame)stackMapFrame;
			List<VerificationType> locals = new ArrayList<VerificationType>();
			for (AbstractStackmapVariableinfo info: slo.getLocals()) {
				locals.add(createVerificationTypeFromStackMap(info));
			}
			List<VerificationType> stack = new ArrayList<VerificationType>();
			for (AbstractStackmapVariableinfo info: slo.getStackItems()) {
				stack.add(createVerificationTypeFromStackMap(info));
			}
			return previousFrame.applyStackmapFull(locals, stack);
		} else {
			throw new IllegalArgumentException("unknown stackmap frame type: "+stackMapFrame);
		}
	}
	
	private VerificationType createVerificationTypeFromStackMap(AbstractStackmapVariableinfo info) {
		if (info instanceof DoubleStackmapVariableinfo) {
			return VerificationType.DOUBLE;
		} else if (info instanceof FloatStackmapVariableinfo) {
			return VerificationType.FLOAT;
		} else if (info instanceof IntegerStackmapVariableinfo) {
			return VerificationType.INT;
		} else if (info instanceof LongStackmapVariableinfo) {
			return VerificationType.LONG;
		} else if (info instanceof NullStackmapVariableinfo) {
			return VerificationType.NULL;
		} else if (info instanceof ObjectStackmapVariableinfo) {
			ObjectStackmapVariableinfo osvi = (ObjectStackmapVariableinfo)info;
			ClassInfo cli = osvi.getClassInfo();
			if (cli.isArray()) {
				return new ObjectValueType(new TypeDescriptor(cli.getClassName()), this);
			} else {
				return new ObjectValueType(new TypeDescriptor("L"+cli.getClassName()+";"), this);
			}
		} else if (info instanceof TopStackmapVariableinfo) {
			return VerificationType.TOP;
		} else if (info instanceof UninitializedStackmapVariableinfo) {
			UninitializedStackmapVariableinfo usv = (UninitializedStackmapVariableinfo)info;
			ConstantPoolInstruction instr = usv.getInstruction(); 
			ClassInfo cli = (ClassInfo)instr.getCpEntry();
			if (cli.isArray()) {
				throw new IllegalStateException("new isn't allowed for class types");
			}
			return new UninitializedValueType(new TypeDescriptor("L"+cli.getClassName()+";"), instr.getIndex());
		} else if (info instanceof UninitializedThisStackmapVariableinfo) {
			return VerificationType.UNINITIALIZED_THIS;
		} else {
			throw new IllegalArgumentException("Unknown stackmap variable info: "+info);
		}
	}
	
	private void checkForBadCode() {
		for (int i=0;i<parent.getSize(); i++) {
			AbstractInstruction instr = parent.get(i);
			checkForBadCode(instr);
		}
	}
	
	
	
	private void calculateFollowers() {
		followers = new ArrayList<Set<Integer>>();
		exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
		
		//Normal followers
		for (int i=0;i<parent.getSize(); i++) {
			AbstractInstruction instr = parent.get(i);
			Set<Integer> instrFollowers = new HashSet<Integer>();
			followers.add(instrFollowers);
			Set<ExceptionHandler> instrHandlers = new HashSet<ExceptionHandler>();
			exceptionHandlers.add(instrHandlers);
			if (instr instanceof BranchInstruction) {
				BranchInstruction bi = (BranchInstruction)instr;
				if (instr.getOpCode() == OpCodes.jsr || instr.getOpCode() == OpCodes.jsr_w) {
					//No Subroutines
				} else {
					instrFollowers.add(bi.getTargetInst().getIndex());
					branchTargets.add(bi.getTargetInst().getIndex());
				}
				if (instr.getOpCode() == OpCodes.goto_ || instr.getOpCode() == OpCodes.goto_w) {
					
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(nextIndex-1);
					} else {
						instrFollowers.add(nextIndex);
					}
				}
			} else if (instr instanceof AbstractSwitchInstruction) {
				AbstractSwitchInstruction ai = (AbstractSwitchInstruction)instr;
				instrFollowers.add(ai.getDefaultTarget().getIndex());
				branchTargets.add(ai.getDefaultTarget().getIndex());
				for (AbstractInstruction instr1: ai.getTargets()) {
					instrFollowers.add(instr1.getIndex());
					branchTargets.add(instr1.getIndex());
				}
			} else if (instr instanceof ArgumentLessInstruction) {
				ArgumentLessInstruction ai = (ArgumentLessInstruction)instr;
				if (ai.isReturn() || ai.getOpCode() == OpCodes.athrow) {
					//Returns and throws don't have followers
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(nextIndex-1);
					} else {
						instrFollowers.add(nextIndex);
					}
					
				}
			} else if (instr instanceof LocalVariableInstruction ) {
				LocalVariableInstruction li = (LocalVariableInstruction)instr;
				if (li.getOpCode() == OpCodes.ret) {
					//Subroutines have a special handling
				} else {
					int nextIndex = instr.getIndex()+1;
					if (nextIndex>=parent.getSize()) {
						throw new FallOffException(instr.getIndex());
					} else {
						instrFollowers.add(nextIndex);
					}
				}
			} else {
				int nextIndex = instr.getIndex()+1;
				if (nextIndex>=parent.getSize()) {
					throw new FallOffException(instr.getIndex());
				} else {
					instrFollowers.add(nextIndex);
				}
			}
		}
		
		//Exception handlers
		ExceptionHandlerTable table = ((CodeAttributeContent)parent.getParent()).getExceptionTable();
		for (int i=0;i<table.getSize(); i++) {
			ExceptionHandler handler = table.get(i);
			exceptionTargets.add(handler.getHandlerInstruction().getIndex());
			for (int j=handler.getStartInstruction().getIndex(); j<=handler.getEndInstruction().getIndex();  j++) {
				exceptionHandlers.get(j).add(handler);
			}
		}
	}
	
	private void checkAllReachable() {
		Set<Integer> reachables = getAllReachable(0);
		for (int i=0;i<parent.getSize(); i++) {
			if (!reachables.contains(i)) {
				throw new VerifyException(i, "dead code");
			}
		}
	}
	
	
	
	private void checkForBadCode(AbstractInstruction instr) {
		if (instr.getOpCode() == OpCodes.jsr || 
			instr.getOpCode() == OpCodes.jsr_w||
			instr.getOpCode() == OpCodes.ret) {
			throw new BadCodeException(instr.getIndex());
		}
	}

	@Override
	public boolean isInterface(String className) {
		ExternalClassInfo classInfo = getClass(className);
		return classInfo.getModifier().isInterface();
	}
	
	//Class Query

	@Override
	public boolean isAssignable(String classTo, String classFrom) {
		
		ExternalClassInfo classToInfo = getClass(classTo);
		ExternalClassInfo classFromInfo = getClass(classFrom);

		return classFromInfo.isAssignableTo(classToInfo);
	}

	@Override
	public String merge(String classTo, String classFrom) {
		if (classTo.equals(classFrom)) {
			return classTo;
		} else if (classTo.equals("java/lang/Object") || classFrom.equals("java/lang/Object")) {
			return "java/lang/Object";
		} else {
			
			while (!isAssignable(classTo, classFrom)) {
				ExternalClassInfo cl = getClass(classTo);
				classTo = cl.getSuperName();
			}
			return classTo;
		}
	}
	
	private ExternalClassInfo getClass(String name) {
		IErrorEmitter dummyEmitter = new IErrorEmitter() {
			
			@Override
			public void emitErrorOnLocation(SourceLocation sl, String message) {
				
				
			}
			
			@Override
			public void emitError(AbstractLiteral literal, String message) {
				
				
			}
		};
		ExternalClassInfo result = clazz.checkAndLoadClassInfo(dummyEmitter, null, name, false);
		if (result == null) {
			throw new UnknownClassException(-1, name);
		}
		return result;
	}
	
	private void emitCodeVerifyError(VerifyException e) {
		int index = 0;
		if (e.getInstructionIndex()>=0) {
			index = e.getInstructionIndex();
		} else if (currentInstructionIndex>=0) {
			index = currentInstructionIndex;
		} 
		AbstractInstruction instr = getInstructionAt(index);
		instr.emitError(null, "code verification error - "+e.getMessage());
		
	}
	
	private void emitRuntimeError(RuntimeException e) {
		//e.printStackTrace();
		int index = 0;
		if (currentInstructionIndex>=0) {
			index = currentInstructionIndex;
		} 
		AbstractInstruction instr = getInstructionAt(index);
		instr.emitError(null, "code verification runtime error - "+e.getClass().getName()+":"+e.getMessage());
		
	}

	public Method getMethod() {
		return method;
	}

	public Clazz getClazz() {
		return clazz;
	}
	
	
	
	
}
