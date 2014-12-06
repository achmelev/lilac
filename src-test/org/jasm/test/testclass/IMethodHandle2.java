package org.jasm.test.testclass;

public interface IMethodHandle2 {

	public abstract void initHandles();

	public abstract void callSetA1(int a);

	public abstract void callSetA2(int a);

	public abstract void callSetA3(int a);

	public abstract void callPutA(int a);

	public abstract int callGetA();

	public abstract void callSetB(int b);

	public abstract void callPutB(int b);

	public abstract int callGetB();
	
	public int  callConstructor(int a);

}