package org.jasm.test.playground;

import org.jasm.test.playground.bean.IntB;

public class InvokeConstructorExecutable implements IExecutable {

	@Override
	public void execute() {
		System.out.println(new IntB().value);
		
	}

}
