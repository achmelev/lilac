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
	public void testStakmap() {
		ClassLoader cl = createClassLoader();
		try {
			Class clazz = cl.loadClass(StackmapProber.class.getName());
			Prober prob = (Prober)clazz.newInstance();
			prob.check(3);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private ClassLoader createClassLoader() {
		return new AssemblerClassLoader(Thread.currentThread().getContextClassLoader());
	}

}
