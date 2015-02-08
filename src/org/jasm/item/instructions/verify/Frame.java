package org.jasm.item.instructions.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.jasm.item.instructions.verify.error.InconsistentRegisterValueException;
import org.jasm.item.instructions.verify.error.InconsistentStackSizeException;
import org.jasm.item.instructions.verify.error.InconsistentStackValueException;
import org.jasm.item.instructions.verify.error.StackOverflowException;
import org.jasm.item.instructions.verify.error.UnexpectedStackTypeException;
import org.jasm.item.instructions.verify.types.VerificationType;

public class Frame {
	
	private int maxStackSize;
	
	private List<VerificationType> locals;
	private Stack<VerificationType> stack;
	
	public Frame(int maxLocals, int maxStackSize) {
		this.maxStackSize = maxStackSize;
		for (int i=0;i<maxLocals; i++) {
			locals.add(VerificationType.TOP);
		}
	}
	
	private Frame(List<VerificationType> locals, Stack<VerificationType> stack, int maxStackSize) {
		this.locals = new ArrayList<VerificationType>();
		this.locals.addAll(locals);
		this.stack = new Stack<VerificationType>();
		this.stack.addAll(stack);
		this.maxStackSize = maxStackSize;
	}
	

	public void push(VerificationType type) {
		if (stack.size() == maxStackSize) {
			throw new StackOverflowException(-1);
		}
		stack.push(type);
	}
	
	public VerificationType pop(VerificationType expected) {
		if (stack.size() == 0) {
			throw new StackOverflowException(-1);
		}
		VerificationType value = stack.pop();
		if (expected.isAssignableFrom(value)) {
		   return value;
		} else {
			throw new UnexpectedStackTypeException(-1, expected, value);
		}
	}
	
	public VerificationType load(VerificationType expected, int register) {
		if (register<=0 || register>=locals.size()) {
			throw new IllegalArgumentException("illegal register index: "+register);
		}
		if ((expected.equals(VerificationType.DOUBLE) || expected.equals(VerificationType.LONG) 
				&& register>=locals.size()-1))  {
			throw new IllegalArgumentException("illegal register index for two word types: "+register);
		}
		
		VerificationType value = locals.get(register);
		if (!expected.isAssignableFrom(value)) {
			throw new InconsistentRegisterValueException(-1, register, expected, value);
		}
		push(value);
		return value;
	}
	
	public void store(VerificationType expected, int register) {
		if (register<=0 || register>=locals.size()) {
			throw new IllegalArgumentException("illegal register index: "+register);
		}
		if ((expected.equals(VerificationType.DOUBLE) || expected.equals(VerificationType.LONG) 
				&& register>=locals.size()-1))  {
			throw new IllegalArgumentException("illegal register index for two word types: "+register);
		}
		VerificationType value = pop(expected);
		locals.set(register, value);
		if ((value.equals(VerificationType.DOUBLE) || value.equals(VerificationType.LONG)))  {
			locals.set(register+1, VerificationType.TOP);
		} else {
			if (register>0 && 
					(locals.get(register-1).equals(VerificationType.DOUBLE) || locals.get(register-1).equals(VerificationType.LONG))) {
				locals.set(register-1,VerificationType.TOP);
			}
		}
		
	}
	
	public void isAssignableFrom(Frame other) {
		if (other.locals.size() !=this.locals.size()) {
			throw new IllegalArgumentException("inconsistent locals sizes "+other.locals.size()+"!="+locals.size());
		}
		if (other.stack.size() != this.stack.size()) {
			throw new InconsistentStackSizeException(-1, other.stack.size(), this.stack.size());
		}
		
		for (int i=0;i<this.stack.size(); i++) {
			if (!this.stack.get(i).isAssignableFrom(other.stack.get(i))) {
				throw new InconsistentStackValueException(-1, i, this.stack.get(i), other.stack.get(i));
			}
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			if (!this.locals.get(i).isAssignableFrom(other.locals.get(i))) {
				throw new InconsistentStackValueException(-1, i, this.locals.get(i), other.locals.get(i));
			}
		}
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



	public boolean theSame(Frame  other) {
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
			if (!this.locals.get(i).isAssignableFrom(other.locals.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public int getCurrentStackSize() {
		return stack.size();
	}
	
	public void replaceAllOccurences(VerificationType oldValue,VerificationType newValue) {
		for (int i=0;i<this.stack.size(); i++) {
			if (this.stack.get(i).equals(oldValue)) {
				this.stack.set(i,newValue);
			}
		}
		
		for (int i=0;i<this.locals.size(); i++) {
			if (this.locals.get(i).equals(oldValue)) {
				this.locals.set(i,newValue);
			}
		}
	}
	
	
	

}
