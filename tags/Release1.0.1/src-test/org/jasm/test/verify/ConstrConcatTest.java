package org.jasm.test.verify;

import junit.framework.Assert;

import org.jasm.loader.AssemblerClassLoader;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierInterfaceContext;
import org.jasm.test.playground.IExecutable;
import org.jasm.type.verifier.VerifierParams;
import org.junit.Test;

public class ConstrConcatTest {

	@Test
	public void test() {
		
		
		String name = "org.jasm.test.verify.ConstrConcat";
		
		
		ClassLoader loader = new AssemblerClassLoader(Thread.currentThread().getContextClassLoader(), true);
		IConstrConcat concat = null;
		try {
			concat = (IConstrConcat) loader.loadClass(name).newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		concat.setContent();
		Assert.assertEquals("Helllo world! : 0", concat.getContent());

	}

}
