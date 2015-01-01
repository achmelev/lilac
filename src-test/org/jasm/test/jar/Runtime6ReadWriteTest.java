package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Runtime6ReadWriteTest extends
		AbstractReadWriteJarTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}

	

	

	@Override
	protected File getFile() {
		return new File("c:/Programme/Java/jdk1.6.0_16/jre/lib/rt.jar");
	}





	@Override
	protected void logStatus() {
		
		
	}
	
	@Override
	protected boolean filter(String name) {
		//return name.equals("java/util/ResourceBundle.class");
		return true;
	}
	
	
}
