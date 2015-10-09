package org.jasm.test.testclass;

public class HelloWorld {
	
	public static void main(String[] args) {
		System.out.println(new HelloWorld().getMessage());
	}
	
	@Deprecated
	public String getMessage() {
		varArgsMethod("a","a","b");
		return "Hello \n World!";
	}
	
	public void varArgsMethod(Object... args) {
		
	}

}
