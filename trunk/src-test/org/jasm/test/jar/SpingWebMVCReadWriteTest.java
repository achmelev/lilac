package org.jasm.test.jar;

import org.junit.Test;

public class SpingWebMVCReadWriteTest extends
		AbstractReadWriteHttpJarTest {


	@Override
	protected String getURL() {
		return "http://central.maven.org/maven2/org/springframework/spring-webmvc/4.0.6.RELEASE/spring-webmvc-4.0.6.RELEASE.jar";
	}
	
	@Test
	public void test() {
		doJarTest();
	}

}
