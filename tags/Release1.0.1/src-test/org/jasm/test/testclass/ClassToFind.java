package org.jasm.test.testclass;

public abstract class ClassToFind implements Runnable {
	
	private int intField;
	public final Double doubleField = new Double(0);
	
	public abstract Double add(float a, float b);
	
	private final synchronized boolean checkString(String t) {
		return t.length() == 0;
	}

}
