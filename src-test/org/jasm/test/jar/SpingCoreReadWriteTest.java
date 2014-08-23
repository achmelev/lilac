package org.jasm.test.jar;

import org.junit.Test;

public class SpingCoreReadWriteTest extends
		AbstractReadWriteHttpJarTest {


	@Override
	protected String getURL() {
		return "http://central.maven.org/maven2/org/springframework/spring-core/4.0.6.RELEASE/spring-core-4.0.6.RELEASE.jar";
	}
	
	@Test
	public void test() {
		doJarTest();
	}

}
