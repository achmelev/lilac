package org.jasm.test.playground;



import org.jasm.test.playground.calc.Calculator;

public class AccessProber implements Prober {

	@Override
	public int check(int a) {

		return Calculator.calculate(a);
		
	}

}
