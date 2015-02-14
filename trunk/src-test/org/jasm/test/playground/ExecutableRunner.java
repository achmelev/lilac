package org.jasm.test.playground;

import org.jasm.loader.AssemblerClassLoader;
import org.jasm.type.verifier.VerifierParams;

public class ExecutableRunner {

	public static void main(String[] args) {
		
		try {
			String name = args[0];
			VerifierParams params = null;
			if (args.length > 1) {
				params = new VerifierParams();
			}
			ClassLoader loader = new AssemblerClassLoader(Thread.currentThread().getContextClassLoader(), params);
			IExecutable executable = (IExecutable) loader.loadClass(name).newInstance();
			executable.execute();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		} 
		System.exit(0);

	}

}
