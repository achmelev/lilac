package org.jasm.test.playground;

public class InvokeWithInterfaceArgumentExecutable implements IExecutable {

	@Override
	public void execute() {
	
		Object o = new Object();
		int  i = 3;
		if (i>5) {
			inv((Runnable)o);
		} else {
			inv((Runnable)o);
		}
		
	}
	
	public void inv(Runnable r) {
		r.run();
	}

}
