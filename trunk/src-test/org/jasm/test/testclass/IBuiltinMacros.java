package org.jasm.test.testclass;

public interface IBuiltinMacros {
	
	public String concat(String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
	public Boolean box(boolean a);
	
	public Integer boxZ2I(boolean a);
	
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
	
	/* OBJECT TO PRIMITIVE CASTS */ 
	
	public byte opconvert1(Boolean p);
	
	public byte opconvert2(Character p);
	
	public byte opconvert3(Double p); 
	
	public byte opconvert4(Float p); 
	
	public byte opconvert5(Integer p);

	public byte opconvert6(Long p);
	
	public byte opconvert7(Short p); 
	
	public boolean opconvert8(Byte p); 
	
	public boolean opconvert9(Character p); 
	
	public boolean opconvert10(Double p);
	
	public boolean opconvert11(Float p); 
	
	public boolean opconvert12(Integer p);
	
	public boolean opconvert13(Long p);
	
	public boolean opconvert14(Short p); 
	
	public char opconvert15(Byte p); 
	
	public char opconvert16(Boolean p); 
	
	public char opconvert17(Double p);
	
	public char opconvert18(Float p); 
	
	public char opconvert19(Integer p);
	
	public char opconvert20(Long p); 
	
	public char opconvert21(Short p); 
	
	public double opconvert22(Byte p); 
	
	public double opconvert23(Boolean p);
	
	public double opconvert24(Character p); 
	
	public double opconvert25(Float p);
	
	public double opconvert26(Integer p); 
	
	public double opconvert27(Long p);
	
	public double opconvert28(Short p);
	
	public float opconvert29(Byte p);
	
	public float opconvert30(Boolean p);
	
	public float opconvert31(Character p);
	
	public float opconvert32(Double p); 
	
	public float opconvert33(Integer p);
	
	public float opconvert34(Long p);
	
	public float opconvert35(Short p);
	
	public int opconvert36(Byte p); 
	
	public int opconvert37(Boolean p);
	
	public int opconvert38(Character p);
	
	public int opconvert39(Double p); 
	
	public int opconvert40(Float p); 
	
	public int opconvert41(Long p); 
	
	public int opconvert42(Short p); 
	
	public long opconvert43(Byte p);
	
	public long opconvert44(Boolean p); 
	
	public long opconvert45(Character p); 
	
	public long opconvert46(Double p); 
	
	public long opconvert47(Float p);
	
	public long opconvert48(Integer p);
	
	public long opconvert49(Short p);
	
	public short opconvert50(Byte p); 
	
	public short opconvert51(Boolean p);
	
	public short opconvert52(Character p); 
	
	public short opconvert53(Double p); 
	
	public short opconvert54(Float p);
	
	public short opconvert55(Integer p);
	
	public short opconvert56(Long p);
	
	public byte unbox(Byte p);
	
	public boolean unbox(Boolean p);
	
	public char unbox(Character p);
	
	public double unbox(Double p);
	
	public float unbox(Float p);
	
	public int unbox(Integer p);
	
	public long unbox(Long p);
	
	public short unbox(Short p);
	
	public byte nunbox1(Number p);
	
	public boolean nunbox2(Number p);
	
	public char nunbox3(Number p);
	
	public double nunbox4(Number p); 
	
	public float nunbox5(Number p); 
	
	public int nunbox6(Number p); 
	
	public long nunbox7(Number p); 
	
	public short nunbox8(Number p); 
}
