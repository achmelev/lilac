package org.jasm.test.testclass;

public class HelloWorld {
	
	public static void main(String[] args) {
		System.out.println(new HelloWorld().getMessage());
	}
	
	@Deprecated
	public String getMessage() {
		return "Hello \n World!";
	}

}
