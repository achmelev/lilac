package org.jasm.test.testclass;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

public class MethodHandleExample implements IMethodHandle1, IMethodHandle2 {
	
	private int a;
	private static int b;
	
	public MethodHandle m_setA1;
	public MethodHandle m_setA2;
	public MethodHandle m_setA3;
	public MethodHandle m_putA;
	public MethodHandle m_getA;
	public MethodHandle m_constructor;
	public MethodHandle m_setB;
	public MethodHandle m_putB;
	public MethodHandle m_getB;
	
	private void setA1(int a) {
		this.a = a;
	}
	
	public void setA2(int a) {
		this.a = a;
	}
	
	public void setA3(int a) {
		this.a = a;
	}
	
	public static void setB(int b1) {
		b = b1;
	}
	
	public MethodHandleExample() {
		
	}
	
	public MethodHandleExample(int a) {
		this.a = a;
	}
	
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#initHandles()
	 */
	@Override
	public void initHandles() {
		Lookup lookup = MethodHandles.lookup();
		
		try {
			m_setA1 = lookup.findSpecial(MethodHandleExample.class, "setA1", MethodType.methodType(void.class, int.class), MethodHandleExample.class);
			m_setA2 = lookup.findVirtual(MethodHandleExample.class, "setA1", MethodType.methodType(void.class, int.class));
			m_setA3 = lookup.findVirtual(MethodHandleExample.class, "setA3", MethodType.methodType(void.class, int.class));
			m_putA = lookup.findSetter(MethodHandleExample.class, "a", int.class);
			m_getA = lookup.findGetter(MethodHandleExample.class, "a", int.class);
			m_setB = lookup.findStatic(MethodHandleExample.class, "setB", MethodType.methodType(void.class, int.class));
			m_putB = lookup.findStaticSetter(MethodHandleExample.class, "b", int.class);
			m_getB = lookup.findStaticGetter(MethodHandleExample.class, "b", int.class);
			m_constructor = lookup.findConstructor(MethodHandleExample.class, MethodType.methodType(void.class, int.class));
		} catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callSetA1(int)
	 */
	@Override
	public void callSetA1(int a) {
		try {
			m_setA1.invokeExact(this,a);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callSetA2(int)
	 */
	@Override
	public void callSetA2(int a) {
		try {
			m_setA2.invokeExact(this,a);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callSetA3(int)
	 */
	@Override
	public void callSetA3(int a) {
		try {
			IMethodHandle1 intf = this;
			m_setA3.invokeExact(intf,a);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callPutA(int)
	 */
	@Override
	public void callPutA(int a) {
		try {
			m_putA.invokeExact(this,a);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callGetA()
	 */
	@Override
	public int  callGetA() {
		try {
			return (int)m_getA.invokeExact(this);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int  callConstructor(int a) {
		try {
			MethodHandleExample inst = (MethodHandleExample)m_constructor.invokeExact(a);
			return inst.a;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callSetB(int)
	 */
	@Override
	public void  callSetB(int b) {
		try {
			m_setB.invokeExact(b);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callPutB(int)
	 */
	@Override
	public void  callPutB(int b) {
		try {
			m_putB.invokeExact(b);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jasm.test.testclass.IMethodHandle2#callGetB()
	 */
	@Override
	public int  callGetB() {
		try {
			return (int)m_getB.invokeExact();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		MethodHandleExample h = new MethodHandleExample();
		h.initHandles();
		System.out.println(h.callConstructor(3));
	}
	
	
	

}
