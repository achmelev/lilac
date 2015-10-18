package org.jasm.tools.print;

public class ConsolePrinter implements IPrinter {
	
	private int counter = 0;

	@Override
	public void printError(String message) {
		System.out.println("error: "+message);
		counter++;
		
	}

	@Override
	public void printWarning(String message) {
		System.out.println("warning: "+message);
		
	}

	@Override
	public void printInfo(String message) {
		System.out.println(message);
		
	}

	@Override
	public int getErrorCounter() {
		return counter;
	}
	
	

}
