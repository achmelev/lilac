package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.jasm.item.attribute.AbstractStackmapFrame;
import org.jasm.item.attribute.SameExtendedStackmapFrame;
import org.jasm.item.attribute.SameStackmapFrame;
import org.jasm.item.instructions.verify.error.InconsistentStackSizeException;
import org.jasm.item.instructions.verify.error.LocalVariablesMissingException;
import org.jasm.item.instructions.verify.error.StackOverflowException;
import org.jasm.item.instructions.verify.error.StackUnderflowException;
import org.jasm.item.instructions.verify.error.StackmapAppendOverflowException;
import org.jasm.item.instructions.verify.error.StackmapChopUnderflowException;
import org.jasm.item.instructions.verify.error.StackmapFullLocalsOverflowException;
import org.jasm.item.instructions.verify.error.StackmapFullStackOverflowException;
import org.jasm.item.instructions.verify.error.StackmapSameLocalsStackOverflowException;
import org.jasm.item.instructions.verify.error.UnexpectedRegisterTypeException;
import org.jasm.item.instructions.verify.error.UnexpectedStackTypeException;
import org.jasm.item.instructions.verify.types.IClassQuery;
import org.jasm.item.instructions.verify.types.ObjectValueType;
import org.jasm.item.instructions.verify.types.VerificationType;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class Frame {
	
	private int maxStackSize;
	
	private List<VerificationType> locals;
	private Stack<VerificationType> stack;
	
	private int currentStackSize = 0;
	
	private int activeLocals = 0;
	
	public Frame(int maxLocals, int maxStackSize) {
		locals = new ArrayList<VerificationType>();
		stack = new Stack<VerificationType>();
		this.maxStackSize = maxStackSize;
		for (int i=0;i<maxLocals; i++) {
			locals.add(VerificationType.TOP);
		}
		this.activeLocals = calculateActiveLocals();
	}
	
	private Frame(List<VerificationType> locals, Stack<VerificationType> stack, int maxStackSize) {
		this.locals = new ArrayList<VerificationType>();
		this.locals.addAll(locals);
		this.stack = new Stack<VerificationType>();
		this.stack.addAll(stack);
		this.maxStackSize = maxStackSize;
		this.activeLocals = calculateActiveLocals();
		this.currentStackSize = 0;
		for (VerificationType t: stack) {
			this.currentStackSize+=t.getSize();
		}
		if (currentStackSize>maxStackSize) {
			throw new IllegalArgumentException(currentStackSize+">"+maxStackSize);
		}
	}
	

	public void push(VerificationType type) {
		if (currentStackSize+type.getSize() > maxStackSize) {
			throw new StackOverflowException(-1);
		}
		currentStackSize+=type.getSize();
		stack.push(type);
	}
	
	public VerificationType pop(VerificationType expected) {
		if (stack.size() == 0) {
			throw new StackUnderflowException(-1);
		}
		VerificationType value = stack.peek();
		
		if (expected.isAssignableFrom(value)) {
			stack.pop();
			currentStackSize-=value.getSize();
			return value;
		} else {
			throw new UnexpectedStackTypeException(-1, stack.size()-1,expected, value);
		}
	}
	
	public VerificationType peek(VerificationType expected) {
		if (stack.size() == 0) {
			throw new StackUnderflowException(-1);
		}
		VerificationType value = stack.peek();
		
		if (expected.isAssignableFrom(value)) {
			return value;
		} else {
			throw new UnexpectedStackTypeException(-1, stack.size()-1,expected, value);
		}
	}
	
	public VerificationType load(VerificationType expected, int register) {
		if (register<0 || register>=locals.size()) {
			throw new IllegalArgumentException("illegal register index: "+register);
		}
		if ((expected.getSize() == 2 
				&& register>=locals.size()-1))  {
			throw new IllegalArgumentException("illegal register index for two word types: "+register);
		}
		
		VerificationType value = locals.get(register);
		if (!expected.isAssignableFrom(value)) {
			throw new UnexpectedRegisterTypeException(-1, register, expected, value);
		}
		push(value);
		return value;
	}
	
	public Frame throwException(ObjectValueType t) {
		if (VerificationType.THROWABLE.create(t.getQuery()).isAssignableFrom(t)) {
			stack.clear();
			stack.push(t);
		} else {
			throw new IllegalStateException(t.toString());
		}
		return this;
	}
	
	public void checkRegister(int register, VerificationType expected) {
		VerificationType value = locals.get(register);
		if (!expected.isAssignableFrom(value)) {
			throw new UnexpectedRegisterTypeException(-1, register, expected, value);
		}
	}
	
	public boolean isOnStack(VerificationType t) {
		for (int i=0;i<stack.size(); i++) {
			if (stack.get(i).equals(t)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void store(VerificationType expected, int register) {
		VerificationType value = pop(expected);
		setRegisterValue(value, register);
		
	}
	
	private void setRegisterValue(VerificationType value, int register) {
		if (register<0 || register>=locals.size()) {
			throw new IllegalArgumentException("illegal register index: "+register);
		}
		if (value.getSize() == 2
				&& register>=locals.size()-1)  {
			throw new IllegalArgumentException("illegal register index for two word types: "+register);
		}
		locals.set(register, value);
		int activeLocalsCandidate = register+1;
		if (value.getSize() == 2)  {
			locals.set(register+1, VerificationType.TOP);
			activeLocalsCandidate++;
		} else {
			if (register>0 && 
					(locals.get(register-1).equals(VerificationType.DOUBLE) || locals.get(register-1).equals(VerificationType.LONG))) {
				locals.set(register-1,VerificationType.TOP);
			}
		}
		
		activeLocals = Math.max(activeLocalsCandidate, activeLocals);
	}
	
	public void replaceAllRegisterOccurences(VerificationType oldValue, VerificationType newValue) {
		for (int i=0;i<locals.size(); i++) {
			if (locals.get(i).equals(oldValue)) {
				setRegisterValue(newValue, i);
			}
		}
	}
	
	public boolean isAssignableFrom(Frame other) {
		if (other.locals.size() !=this.locals.size()) {
			throw new IllegalArgumentException("inconsistent locals sizes "+other.locals.size()+"!="+locals.size());
		}
		if (other.stack.size() != this.stack.size()) {
			throw new InconsistentStackSizeException(-1, other.stack.size(), this.stack.size());
		}
		
		for (int i=0;i<this.stack.size(); i++) {
			if (!this.stack.get(i).isAssignableFrom(other.stack.get(i))) {
				throw new UnexpectedStackTypeException(-1, i, this.stack.get(i), other.stack.get(i));
			}
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			if (!this.locals.get(i).isAssignableFrom(other.locals.get(i))) {
				throw new UnexpectedRegisterTypeException(-1, i, this.locals.get(i), other.locals.get(i));
			}
		}
		
		return true;
	}
	
	public Frame merge(Frame other) {
		if (other.locals.size() !=this.locals.size()) {
			throw new IllegalArgumentException("inconsistent locals sizes "+other.locals.size()+"!="+locals.size());
		}
		if (other.stack.size() != this.stack.size()) {
			throw new InconsistentStackSizeException(-1, other.stack.size(), this.stack.size());
		}
		
		Stack newStack = new Stack();
		List newLocals = new ArrayList();
		
		for (int i=0;i<this.stack.size(); i++) {
			newStack.add(this.stack.get(i).mergeWith(other.stack.get(i)));
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			newLocals.add(this.locals.get(i).mergeWith(other.locals.get(i)));
		}
		
		return new Frame(newLocals,newStack, maxStackSize);
	}
	
	public Frame copy() {
		return new Frame(locals,stack,maxStackSize);
	}



	public boolean equals(Frame  other) {
		if (other.locals.size() !=this.locals.size()) {
			throw new IllegalArgumentException("inconsistent locals sizes "+other.locals.size()+"!="+locals.size());
		}
		if (other.stack.size() != this.stack.size()) {
			return false;
		}
		
		for (int i=0;i<this.stack.size(); i++) {
			if (!this.stack.get(i).equals(other.stack.get(i))) {
				return false;
			}
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			if (!this.locals.get(i).equals(other.locals.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public int getCurrentStackSize() {
		return currentStackSize;
	}
	
	public int getActiveLocals() {
		return activeLocals;
	}

	public void replaceAllOccurences(VerificationType oldValue,VerificationType newValue) {
		replaceAllRegisterOccurences(oldValue, newValue);
		replaceAllStackOccurences(oldValue, newValue);
	}
	
	public void replaceAllStackOccurences(VerificationType oldValue,VerificationType newValue) {
		for (int i=0;i<this.stack.size(); i++) {
			if (this.stack.get(i).equals(oldValue)) {
				this.stack.set(i,newValue);
			}
		}
	}
	
	public int countAllOccurencies(VerificationType t) {
		int result = 0;
		for (int i=0;i<locals.size(); i++) {
			if (locals.get(i).equals(t)) {
				result++;
			}
		}
		for (int i=0;i<this.stack.size(); i++) {
			if (this.stack.get(i).equals(t)) {
				result++;
			}
		}
		return result;
	}
	
	private int calculateActiveLocals() {
		int result = locals.size();
		while (result>0 && locals.get(result-1) == VerificationType.TOP) {
			result--;
		}
		if (result == 0) {
			return 0;
		} else {
			if (locals.get(result-1).getSize() == 2) {
				if (result == locals.size()) {
					throw new IllegalStateException("two words type in the last register");
				}
				result++;
			}
		}
		return result;
		
	}
	
	public static Frame createInitialFrame(String className, boolean isConstructor, boolean isStatic, int maxLocals,int maxStack,MethodDescriptor desc, IClassQuery classQuery) {
		if (isConstructor && isStatic) {
			throw new IllegalArgumentException("There are no static constructors");
		}
		List<VerificationType> locals = new ArrayList<VerificationType>();
		if (!isStatic) {
			if (isConstructor) {
				locals.add(VerificationType.UNINITIALIZED_THIS);
			} else {
				locals.add(new ObjectValueType(new TypeDescriptor("L"+className+";"), classQuery));
			}
		}
		for (TypeDescriptor typeDesc: desc.getParameters()) {
			VerificationType type = VerificationType.createTypeFromDescriptor(typeDesc, classQuery);
			locals.add(type);
			if (type.getSize()==2) {
				locals.add(VerificationType.TOP);
			}
		}
		if (maxLocals<locals.size()) {
			throw new LocalVariablesMissingException();
		}
		
		Stack<VerificationType> stack = new Stack<VerificationType>();
		return new Frame(appendTopsIgNecessary(locals, maxLocals), stack, maxStack);
	}
	
	public Frame applyStackmapSameLocalsOneStackItem(VerificationType stackItem) {
		Stack<VerificationType> stack = new Stack<VerificationType>();
		stack.push(stackItem);
		if (stackItem.getSize()>maxStackSize) {
			throw new StackmapSameLocalsStackOverflowException(-1, maxStackSize, stackItem.getSize());
		}
		return new Frame(locals, stack,maxStackSize);
	}
	
	public Frame applyStackmapSame() {
		Stack<VerificationType> stack = new Stack<VerificationType>();
		return new Frame(locals, stack,maxStackSize);
	}
	
	private int calculateNumberOfVariables() {
		//calculating current number of vars
		int numberOfVariables = 0;
		for (int i=0;i<activeLocals; i++) {
			if (i==0) {
				numberOfVariables++;
			} else {
				if (!(locals.get(i-1).getSize() == 2)) {
					numberOfVariables++;
				}
			}
		}
		
		return numberOfVariables;
	}
	
	
	
	public Frame applyStackmapChop(int chop) {
		//calculating current number of vars
		int numberOfVariables = calculateNumberOfVariables();
		//checking chop size
		if (chop>numberOfVariables) {
			throw new StackmapChopUnderflowException(-1,activeLocals,chop);
		}
		//Creating new locals 
		List<VerificationType> newLocals = new ArrayList<VerificationType>();
		int newNumberOfVariables = 0;
		int i=0;
		while (newNumberOfVariables<numberOfVariables-chop) {
			newLocals.add(locals.get(i));
			if (locals.get(i).getSize() == 2) {
				newLocals.add(VerificationType.TOP);
				i++;
			}
			newNumberOfVariables++;
			i++;
		}
		
		Stack<VerificationType> stack = new Stack<VerificationType>();
		return new Frame(appendTopsIgNecessary(newLocals, locals.size()), stack,maxStackSize);
		
	}
	
	private static List<VerificationType> appendTopsIgNecessary(List<VerificationType> locals, int maxLocals) {
		int size = locals.size();
		for (int i=0;i<maxLocals-size; i++) {
			locals.add(VerificationType.TOP);
		}
		
		return locals;
	}
	
	
	
	public Frame applyStackmapAppend(List<VerificationType> appendLocals) {
		int appendSize = 0;
		for (VerificationType t: appendLocals) {
			appendSize+=t.getSize();
		}
		if (activeLocals+appendSize>locals.size()) {
			throw new StackmapAppendOverflowException(-1,appendSize, activeLocals,locals.size());
		}
		List<VerificationType> newLocals = new ArrayList<VerificationType>();
		for (int i=0;i<activeLocals; i++) {
			newLocals.add(locals.get(i));
		}
		for (VerificationType t: appendLocals) {
			newLocals.add(t);
			if (t.getSize() == 2) {
				newLocals.add(VerificationType.TOP);
			}
		}
		Stack<VerificationType> stack = new Stack<VerificationType>();
		return new Frame(appendTopsIgNecessary(newLocals, locals.size()), stack,maxStackSize);
		
	}
	
	private int calculateVarsSize(List<VerificationType> vars) {
		int result = 0;
		for (VerificationType t: vars) {
			result+=t.getSize();
		}
		return result;
	}
	
	private List<VerificationType> createLocalsFromVarList(List<VerificationType> vars) {
		List<VerificationType> result = new ArrayList<VerificationType>();
		for (VerificationType t: vars) {
			result.add(t);
			if (t.getSize() == 2) {
				result.add(VerificationType.TOP);
			}
		}
		return appendTopsIgNecessary(result, locals.size());
	}
	
	
	public Frame applyStackmapFull(List<VerificationType> fullLocals, List<VerificationType> fullStack) {
		
		if (calculateVarsSize(fullLocals)>locals.size()) {
			throw new StackmapFullLocalsOverflowException(-1, locals.size(), calculateVarsSize(fullLocals));
		}
		
		if (calculateVarsSize(fullStack)>maxStackSize) {
			throw new StackmapFullStackOverflowException(-1, maxStackSize, calculateVarsSize(fullStack));
		}
		
		List<VerificationType> newLocals = createLocalsFromVarList(fullLocals);
		Stack<VerificationType> stack = new Stack<VerificationType>();
		stack.addAll(fullStack);
		
		return new Frame(newLocals, stack,maxStackSize);
		
	}
	

	public AbstractFrameDifference calculateFrameDifference(Frame nextFrame) {
		
		if (this.maxStackSize != nextFrame.maxStackSize) {
			throw new IllegalArgumentException(this.maxStackSize+"!="+nextFrame.maxStackSize);
		}
		if (this.locals.size() != nextFrame.locals.size()) {
			throw new IllegalArgumentException(this.locals.size()+"!="+nextFrame.locals.size());
		}
		
		if (stack.size() == 0 && nextFrame.stack.size() == 0 && this.equals(nextFrame)) {
			return new SameFrame();
		} else if (stack.size() == 0 && nextFrame.stack.size()==1) {
			boolean sameLocals = true;
			for (int i=0;i<this.locals.size(); i++) {
				if (!this.locals.get(i).equals(nextFrame.locals.get(i))) {
					sameLocals = false;
					break;
				}
			}
			if (sameLocals) {
				return new SameLocalsOneStackItemFrame(nextFrame.stack.get(0));
			} else {
				return nextFrame.createFullFrame();
			}
			
		} else if (stack.size() == 0 && nextFrame.stack.size()==0 && nextFrame.getActiveLocals()<this.getActiveLocals()) {
			boolean sameLocals = true;
			for (int i=0;i<nextFrame.getActiveLocals(); i++) {
				if (!this.locals.get(i).equals(nextFrame.locals.get(i))) {
					sameLocals = false;
					break;
				}
			}
			if (sameLocals) {
				int diff = this.calculateNumberOfVariables()-nextFrame.calculateNumberOfVariables();
				if (diff>=1 && diff<=3) {
					return new ChopFrame(this.calculateNumberOfVariables()-nextFrame.calculateNumberOfVariables());
				} else {
					return nextFrame.createFullFrame();
				}
			} else {
				return nextFrame.createFullFrame();
			}
			
		} else if (stack.size() == 0 && nextFrame.stack.size() == 0 && nextFrame.getActiveLocals()>this.getActiveLocals()) {
			boolean sameLocals = true;
			for (int i=0;i<this.getActiveLocals(); i++) {
				if (!this.locals.get(i).equals(nextFrame.locals.get(i))) {
					sameLocals = false;
					break;
				}
			}
			if (sameLocals) {
				List<VerificationType> l = new ArrayList<VerificationType>();
				for (int i=getActiveLocals();i<nextFrame.getActiveLocals(); i++) {
					if (i==getActiveLocals()) {
						l.add(nextFrame.locals.get(i));
					} else {
						if (!(nextFrame.locals.get(i-1).getSize() == 2)) {
							l.add(nextFrame.locals.get(i));
						}
					}
				}
				if (l.size()>=1 && l.size()<=3) {
					return new AppendFrame(l);
				} else {
					return nextFrame.createFullFrame();
				}
			} else {
				return nextFrame.createFullFrame();
			}
			
		} else {
			return nextFrame.createFullFrame();
		}
			
	}
	
	public FullFrame createFullFrame() {
		List<VerificationType> l = new ArrayList<VerificationType>();
		for (int i=0;i<activeLocals; i++) {
			if (i==0) {
				l.add(locals.get(i));
			} else {
				if (!(locals.get(i-1).getSize() == 2)) {
					l.add(locals.get(i));
				}
			}
		}
		List<VerificationType> s = new ArrayList<VerificationType>();
		s.addAll(stack);
		return new FullFrame(l,s);
	}
	
	/**
	 * Only for Tests
	 */
	public VerificationType getTypeOnStack(int index) {
		return stack.get(index);
	}
	
	public VerificationType getTypeInRegister(int index) {
		return locals.get(index);
	}
	
	public static Frame createFrame(List<VerificationType> locals, List<VerificationType> stackValues, int maxStackSize) {
		List<VerificationType> localValues = new ArrayList<VerificationType>();
		localValues.addAll(locals);
		Stack<VerificationType> values = new Stack<VerificationType>();
		values.addAll(stackValues);
		return new Frame(localValues, values,maxStackSize);
	}
	
	public void updateQuery(IClassQuery query) {
		for (VerificationType t: stack) {
			if (t instanceof ObjectValueType) {
				((ObjectValueType)t).setQuery(query);
			}
		}
		
		for (VerificationType t: locals) {
			if (t instanceof ObjectValueType) {
				((ObjectValueType)t).setQuery(query);
			}
		}
	}

	@Override
	public String toString() {
		return "Frame [locals=" + locals + ", stack=" + stack + "]";
	}
	
	

}
