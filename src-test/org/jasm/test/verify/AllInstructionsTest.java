package org.jasm.test.verify;

import org.jasm.loader.AssemblerClassLoader;
import org.jasm.test.playground.IExecutable;
import org.jasm.type.verifier.VerifierParams;
import org.junit.Test;

public class AllInstructionsTest {

	@Test
	public void test() {
		
		
		String name = "org.jasm.test.verify.AllInstructionsExecutable";
		VerifierParams params = null;
		params = new VerifierParams();
		
		ClassLoader loader = new AssemblerClassLoader(Thread.currentThread().getContextClassLoader(), params);
		IExecutable executable;
		try {
			executable = (IExecutable) loader.loadClass(name).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		executable.execute();

	}

}
