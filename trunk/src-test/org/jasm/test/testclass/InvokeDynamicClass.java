package org.jasm.test.testclass;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class InvokeDynamicClass implements ICalculator {
	
	private static CallSite bootstrapMethod(MethodHandles.Lookup lookup, String name, MethodType type, String param) {
		try {
			MethodHandle handle =  lookup.findVirtual(InvokeDynamicClass.class, name, type);
			CallSite result = new ConstantCallSite(handle);
			return result;
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int add(int a, int b) {
		return a+b;
	}
	
	public int sub(int a, int b) {
		return a-b;
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.ICalculator#calculate()
	 */
	@Override
	public int calculate() {
		return sub(add(10,100),30);
	}

}
