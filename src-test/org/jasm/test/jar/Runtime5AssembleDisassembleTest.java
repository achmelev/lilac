package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Runtime5AssembleDisassembleTest extends
		AbstractAssembleDisassembleJarTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}

	

	

	@Override
	protected File getFile() {
		return new File("c:/Programme/Java/jdk1.5.0_22/jre/lib/rt.jar");
	}
	
	@Override
	protected boolean filter(String name) {
		return !name.contains("package-info")
				/**&& name.equals("com/sun/security/sasl/digest/DigestMD5Client.class")**/;
	}
	
	
}
