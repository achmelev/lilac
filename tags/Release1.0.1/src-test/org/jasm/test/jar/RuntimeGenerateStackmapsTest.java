package org.jasm.test.jar;



import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RuntimeGenerateStackmapsTest extends
		AbstractGenerateStackmapJarTestCase {


	
	
	@Test
	public void test() {
		doJarTest();
	}

	

	@Override
	protected boolean filter(String name) {
		return !name.contains("package-info")
				/*&& name.equals("com/oracle/webservices/internal/api/message/BaseDistributedPropertySet.class")*/;
	}



	
	

	@Override
	protected File getFile() {
		return getFile(java.lang.Object.class);
	}
	
	
}
