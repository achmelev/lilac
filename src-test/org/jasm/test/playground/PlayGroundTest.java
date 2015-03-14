package org.jasm.test.playground;

import org.jasm.loader.AssemblerClassLoader;
import org.junit.Test;

public class PlayGroundTest {
	
	@Test
	public void testCalculator() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(SimpleCalculator.class.getName());
			ICalculator calc = (ICalculator)clazz.newInstance();
			calc.calculate(3, 4);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testStackmap() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(StackmapProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testArray() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(ArrayProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testException() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(ExceptionProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testAccess() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(AccessProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testField() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(FieldProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Test
	public void testInterface() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(ICalculator.class.getName()+"2");
			
		} catch (ClassNotFoundException  e) {
			throw new RuntimeException(e);
		}
	}
	

	
	private ClassLoader createClassLoader() {
		return new AssemblerClassLoader(Thread.currentThread().getContextClassLoader(), false);
	}

}
