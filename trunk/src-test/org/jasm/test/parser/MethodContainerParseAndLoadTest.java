package org.jasm.test.parser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.jasm.item.clazz.Clazz;
import org.jasm.test.testclass.ICalculator;
import org.jasm.test.testclass.IMethodHandle2;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore
public class MethodContainerParseAndLoadTest extends AbstractParseAndLoadTestCase {
	
	@Test
	public void test() {
		super.doTest();
	}

	@Override
	protected String getDateiName() {
		return "MethodContainer.jasm";
	}

	@Override
	protected String getClassName() {
		return "org.jasm.test.playground.MethodContainer2";
	}

	@Override
	protected void testClass(Class cl) {
		 try {
			Object o = cl.newInstance();
			Map<String, Method> methods = new HashMap<String, Method>();
			for (Method m: cl.getDeclaredMethods()) {
				methods.put(m.getName(), m);
			}
			Field f = cl.getDeclaredField("iValue");
			Assert.assertEquals(5, f.get(o));
			
			Method m = methods.get("method2");
			Object [] params = new Object[9];
			params[0] = (byte)5;
			params[1] = true;
			params[2] = 1.5f;
			params[3] = 1.5;
			params[4] = (short)10;
			params[5] = 11;
			params[6] = (long)123;
			params[7] = new int[3];
			params[8] = "dummy";
			Assert.assertEquals(o, m.invoke(o, params));
			
			m = methods.get("method3");
			params = new Object[9];
			params[0] = (byte)5;
			params[1] = true;
			params[2] = 1.5f;
			params[3] = 1.5;
			params[4] = (short)10;
			params[5] = 11;
			params[6] = (long)123;
			params[7] = new String[3];
			params[8] = "dummy";
			Assert.assertEquals(params[7],  m.invoke(o, params));
			
			m = methods.get("method1");
			Assert.assertTrue(Modifier.isAbstract(m.getModifiers()));
			Assert.assertTrue(Modifier.isProtected(m.getModifiers()));
			Assert.assertEquals(Void.TYPE, m.getReturnType());
			Assert.assertEquals(Byte.TYPE, m.getParameterTypes()[0]);
			Assert.assertEquals(Boolean.TYPE, m.getParameterTypes()[1]);
			Assert.assertEquals(Float.TYPE, m.getParameterTypes()[2]);
			Assert.assertEquals(Double.TYPE, m.getParameterTypes()[3]);
			Assert.assertEquals(Short.TYPE, m.getParameterTypes()[4]);
			Assert.assertEquals(Integer.TYPE, m.getParameterTypes()[5]);
			Assert.assertEquals(Long.TYPE, m.getParameterTypes()[6]);
			Assert.assertTrue((m.getParameterTypes()[7]).isArray());
			Assert.assertEquals(Integer.TYPE, m.getParameterTypes()[7].getComponentType());
			Assert.assertEquals(String.class, m.getParameterTypes()[8]);
			
			
		} catch (InstantiationException | IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	protected boolean readAgain() {
		return false;
	}

	@Override
	protected boolean verify() {
		return true;
	}

	@Override
	protected void testReadAraginClass(Clazz cl) {
		
		
	}
	
	

}
