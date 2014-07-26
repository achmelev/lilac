package org.jasm.test.testclass;

public abstract class Class2  {
	
	public static String staticString = "HELLO WORLD";
	
	private final int finalIntField = 0;
	
	protected transient Boolean transientField = false;
	
	public static final int constInt = 1;
	
	
	@Deprecated
	private void privateMethod(int a) {
	}
	
	protected abstract void protectedAbstractMethod();
	
	synchronized static void synchronizedStaticMethod() {
		
	}
	
	public void methodMitException() throws IllegalArgumentException {
		throw new IllegalArgumentException("XXX");
	}
	
	protected void methodWithAnonymousClass() {
		Runnable run = new Runnable() {
			
			@Override
			public void run() {
				
			}
		};
	}
	
	private class InnerClass {
		
	}
	

}
