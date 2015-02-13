package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;




import org.jasm.item.IErrorEmitter;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.ExceptionHandler;
import org.jasm.item.attribute.ExceptionHandlerTable;
import org.jasm.item.attribute.StackMapAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.AbstractSwitchInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.verify.error.BadCodeException;
import org.jasm.item.instructions.verify.error.FallOffException;
import org.jasm.item.instructions.verify.error.MissingStackmapsDeclaration;
import org.jasm.item.instructions.verify.error.UnknownClassException;
import org.jasm.item.instructions.verify.types.IClassQuery;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.type.verifier.VerifierParams;

public class Verifier implements IClassQuery {
	
	private Instructions parent;
	
	private Clazz clazz;
	private Method method;
	private CodeAttributeContent code;
	
	
	private List<Set<Integer>> followers = new ArrayList<Set<Integer>>();
	private List<Set<ExceptionHandler>> exceptionHandlers = new ArrayList<Set<ExceptionHandler>>();
	private Set<Integer> branchTargets = new HashSet<Integer>();
	private Set<Integer> exceptionTargets = new HashSet<Integer>();
	private Map<Integer, Frame> stackMapFrames = new HashMap<Integer, Frame>(); 
	
	public void setParent(Instructions parent) {
		this.parent = parent;
		this.clazz = parent.getAncestor(Clazz.class);
		this.method = parent.getAncestor(Method.class);
		this.code = parent.getAncestor(CodeAttributeContent.class);
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
		calculateFollowers();
		checkForBadCode();
		checkAllReachable();
		double version = clazz.getDecimalVersion().doubleValue();
		if (version>=50) {
			try {
				doTypeChecking();
			} catch (VerifyException e) {
				
				if (version>50) {
					throw e;
				}
			}
		}
		
		
	}
	
	private void doTypeChecking() {
		List<Attribute> attrs = code.getAttributes().getAttributesByContentType(StackMapAttributeContent.class);
		if (attrs.size() == 0 && !(branchTargets.isEmpty() && exceptionTargets.isEmpty())) {
			throw new MissingStackmapsDeclaration();
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
		// TODO Auto-generated method stub
		return false;
	}
	
	//Class Query

	@Override
	public boolean isAssignable(String classTo, String classFrom) {
		
		ExternalClassInfo classToInfo = getClass(classTo);
		ExternalClassInfo classFromInfo = getClass(classFrom);

		return classToInfo.isAssignableTo(classFromInfo);
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
	

}
