package org.jasm.test.testclass;

import java.io.PrintStream;

public interface IBuiltinMacros {
	
	public String concat(String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
	
	public String concat2(String prefix, Byte arg1, Boolean arg2, Character arg3, Double arg4, Float arg5, Integer arg6, Long arg7, Short arg8);
	
	public String concat4(String prefix, Byte arg1, Boolean arg2, Character arg3, Double arg4, Float arg5, Integer arg6, Long arg7, Short arg8);
	
	public String concat5(String prefix, Byte arg1, Boolean arg2, Character arg3, Double arg4, Float arg5, Integer arg6, Long arg7, Short arg8);
	
	
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
	
	/* PRIMITIVE TO OBJECT CASTS */ 
	
	
	public Byte poconvert1(boolean p);
	
	public Byte poconvert2(char p);
	
	public Byte poconvert3(double p); 
	
	public Byte poconvert4(float p); 
	
	public Byte poconvert5(int p);

	
	public Byte poconvert6(long p);
	
	public Byte poconvert7(short p); 
	
	public Boolean poconvert8(byte p); 
	
	public Boolean poconvert9(char p); 
	
	public Boolean poconvert10(double p);
	
	public Boolean poconvert11(float p); 
	
	public Boolean poconvert12(int p);
	
	public Boolean poconvert13(long p);
	
	public Boolean poconvert14(short p); 
	
	public Character poconvert15(byte p); 
	
	public Character poconvert16(boolean p); 
	
	public Character poconvert17(double p);
	
	public Character poconvert18(float p); 
	
	public Character poconvert19(int p);
	
	public Character poconvert20(long p); 
	
	public Character poconvert21(short p); 
	
	public Double poconvert22(byte p); 
	
	public Double poconvert23(boolean p);
	
	public Double poconvert24(char p); 
	
	public Double poconvert25(float p);
	
	public Double poconvert26(int p); 
	
	public Double poconvert27(long p);
	
	public Double poconvert28(short p);
	
	public Float poconvert29(byte p);
	
	public Float poconvert30(boolean p);
	
	public Float poconvert31(char p);
	
	public Float poconvert32(double p); 
	
	public Float poconvert33(int p);
	
	public Float poconvert34(long p);
	
	public Float poconvert35(short p);
	
	public Integer poconvert36(byte p); 
	
	public Integer poconvert37(boolean p);
	
	public Integer poconvert38(char p);
	
	public Integer poconvert39(double p); 
	
	public Integer poconvert40(float p); 
	
	public Integer poconvert41(long p); 
	
	public Integer poconvert42(short p); 
	
	public Long poconvert43(byte p);
	
	public Long poconvert44(boolean p); 
	
	public Long poconvert45(char p); 
	
	public Long poconvert46(double p); 
	
	public Long poconvert47(float p);
	
	public Long poconvert48(int p);
	
	public Long poconvert49(short p);
	
	public Short poconvert50(byte p); 
	
	public Short poconvert51(boolean p);
	
	public Short poconvert52(char p); 
	
	public Short poconvert53(double p); 
	
	public Short poconvert54(float p);
	
	public Short poconvert55(int p);
	
	public Short poconvert56(long p);
	
	public Object box(byte a);
	
	public Object box(boolean a);
	
	public Object box(char a);
	
	public Object box(double a);
	
	public Object box(float a);
	
	public Object box(int a);
	
	public Object box(long a);
	
	public Object box(short a);
	
	public Number nbox(byte a);
	
	public Number nbox(boolean a);
	
	public Number nbox(char a);
	
	public Number nbox(double a);
	
	public Number nbox(float a);
	
	public Number nbox(int a);
	
	public Number nbox(long a);
	
	public Number nbox(short a);
	
	//Object to Object casts
	public int [] [] getIntArray();
	public void setIntArray(int [] [] v);
	
	public MyRunnable getObject();
	
	public void setObject(MyRunnable runnable);
	
	public MyRunnable [] getObjectArray();
	
	public void setObjectArray(MyRunnable[] runnableArray);
	
	public TestBean createTestBean(int intValue, boolean booleanValue, String stringValue, MyRunnable runnable);
	
	public boolean [] createBooleanArray(Double length);
	
	public Runnable [] createRunnableArray(long length);
	
	public boolean [] [] createBooleanArray2(Double length, long length2);
	
	public void println(String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
	
	public void sprintln(PrintStream stream, String prefix, byte arg1, boolean arg2, char arg3, double arg4, float arg5, int arg6, long arg7, short arg8);
}
