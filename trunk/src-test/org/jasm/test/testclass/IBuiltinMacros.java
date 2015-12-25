package org.jasm.test.testclass;

public interface IBuiltinMacros {
	
	public String concat(String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
	public Boolean box(boolean a);
	
	public Integer boxZ2I(boolean a);
	
	public boolean unbox(Boolean a);
	
	public int unboxZ2I(Boolean a);
}
