package org.jasm.test.playground;

import org.jasm.loader.AssemblerClassLoader;
import org.jasm.type.verifier.VerifierParams;

public class ExecutableRunner {

	public static void main(String[] args) {
		
		try {
			String name = args[0];
			VerifierParams params = null;
			
			ClassLoader loader = new AssemblerClassLoader(Thread.currentThread().getContextClassLoader(), (args.length > 1));
			IExecutable executable = (IExecutable) loader.loadClass(name).newInstance();
			executable.execute();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		} 
		System.exit(0);

	}

}
