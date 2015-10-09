package org.jasm.test.playground;

public class InvokeExecutable implements IExecutable, IInvokeExecutable {
	

	@Override
	public void execute() {
		
		
		add(1,2);
		IInvokeExecutable intf = new InvokeExecutable();
		intf.add2(1, 2);
		intf.add3(2, 3);
		System.out.println("Done!");
		
		
	}
	
	public static int add(int a, int b) {
		return a+b;
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.playground.IInvokeExecutable#add2(int, int)
	 */
	@Override
	public int add2(int a, int b) {
		return a+b;
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.playground.IInvokeExecutable#add3(int, int)
	 */
	@Override
	public long add3(int a, int b) {
		return a+b;
	}

}
