package org.jasm.test.playground;

public class CalculatorDelegate {
	
	public int calculate(int a, int b) {
		
		int c = a+b;
		ICalculator calc = new ICalculator() {
			
			@Override
			public int calculate2(int a, int b) {
				return c;
			}
			
			@Override
			public int calculate(int a, int b) {
				return a+b;
			}
		};
		return calc.calculate(a, b);
	}
	
	public int doCalculate(int a, int b) {
		return a+b;
	}

}
