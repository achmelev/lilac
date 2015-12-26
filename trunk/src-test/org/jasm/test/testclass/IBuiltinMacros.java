package org.jasm.test.testclass;

public interface IBuiltinMacros {
	
	public String concat(String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
	public Boolean box(boolean a);
	
	public Integer boxZ2I(boolean a);
	
	public boolean unbox(Boolean a);
	
	public int unboxZ2I(Boolean a);
	
	/* PRIMITIVE CASTS */ 
	
	//byte, boolean, char,double, float, int, long, short
	
	public byte pconvert1(boolean p);
	
	public byte pconvert2(char p);
	
	public byte pconvert3(double p); 
	
	public byte pconvert4(float p); 
	
	public byte pconvert5(int p);

	
	public byte pconvert6(long p);
	
	public byte pconvert7(short p); 
	
	public boolean pconvert8(byte p); 
	
	public boolean pconvert9(char p); 
	
	public boolean pconvert10(double p);
	
	public boolean pconvert11(float p); 
	
	public boolean pconvert12(int p);
	
	public boolean pconvert13(long p);
	
	public boolean pconvert14(short p); 
	
	public char pconvert15(byte p); 
	
	public char pconvert16(boolean p); 
	
	public char pconvert17(double p);
	
	public char pconvert18(float p); 
	
	public char pconvert19(int p);
	
	public char pconvert20(long p); 
	
	public char pconvert21(short p); 
	
	public double pconvert22(byte p); 
	
	public double pconvert23(boolean p);
	
	public double pconvert24(char p); 
	
	public double pconvert25(float p);
	
	public double pconvert26(int p); 
	
	public double pconvert27(long p);
	
	public double pconvert28(short p);
	
	public float pconvert29(byte p);
	
	public float pconvert30(boolean p);
	
	public float pconvert31(char p);
	
	public float pconvert32(double p); 
	
	public float pconvert33(int p);
	
	public float pconvert34(long p);
	
	public float pconvert35(short p);
	
	public int pconvert36(byte p); 
	
	public int pconvert37(boolean p);
	
	public int pconvert38(char p);
	
	public int pconvert39(double p); 
	
	public int pconvert40(float p); 
	
	public int pconvert41(long p); 
	
	public int pconvert42(short p); 
	
	public long pconvert43(byte p);
	
	public long pconvert44(boolean p); 
	
	public long pconvert45(char p); 
	
	public long pconvert46(double p); 
	
	public long pconvert47(float p);
	
	public long pconvert48(int p);
	
	public long pconvert49(short p);
	
	public short pconvert50(byte p); 
	
	public short pconvert51(boolean p);
	
	public short pconvert52(char p); 
	
	public short pconvert53(double p); 
	
	public short pconvert54(float p);
	
	public short pconvert55(int p);
	
	public short pconvert56(long p); 
}
