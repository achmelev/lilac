package org.jasm.test.playground;

public class SimpleCalculator implements ICalculator {
	
	public int calculate(int a, int b) {
		return new CalculatorDelegate().calculate(a, b);
	}
	
	public int calculate2(int a, int b) {
		return a+b;
		
	}

}
