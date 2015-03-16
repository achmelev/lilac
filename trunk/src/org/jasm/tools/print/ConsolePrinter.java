package org.jasm.tools.print;

public class ConsolePrinter implements IPrinter {

	@Override
	public void printError(String message) {
		System.err.println("ERROR: "+message);
		
	}

	@Override
	public void printWarning(String message) {
		System.out.println("WARNING: "+message);
		
	}

	@Override
	public void printInfo(String message) {
		System.out.println(message);
		
	}

}
