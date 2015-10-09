package org.jasm.test.testclass;

public interface Interface1 {
	
	public static String STRING_CONSTANT = "HELLO WORLD";
	public static short  SHORT_CONSTANT = 10;
	public static int    INT_CONSTANT = 1000;
	public static long   LONG_CONSTANT = 10000L;
	public static float  FLOAT_CONSTANT = 10.10f;
	public static double DOUBLE_CONSTANT = 100.10;
	
	public abstract void testMethodWithoutArgs();
	public abstract void testMethod(int a);
	

}
