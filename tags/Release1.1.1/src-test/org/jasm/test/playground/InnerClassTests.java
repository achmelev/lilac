package org.jasm.test.playground;

public class InnerClassTests {
	
	@Deprecated
	public void myMethod() {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		};
		
		class InnerClass3 {
			
		}
	}
	
	private class InnerClass1 {
		private class InnerClass2 {
			
		}
	}

}
