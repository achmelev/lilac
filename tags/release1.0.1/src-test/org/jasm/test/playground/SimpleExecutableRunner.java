package org.jasm.test.playground;


public class SimpleExecutableRunner {

	public static void main(String[] args) {
		
		try {
			String name = args[0];
			IExecutable executable = (IExecutable) Class.forName(name).newInstance();
			executable.execute();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		} 
		System.exit(0);

	}

}
