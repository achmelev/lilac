package org.jasm.test.jar;

import org.junit.Test;

public class SpingWebMVCAssembleDisassembleTest extends
		AbstractAssembleDisassembleHttpJarTestCase {


	@Override
	protected String getURL() {
		return "http://central.maven.org/maven2/org/springframework/spring-webmvc/4.0.6.RELEASE/spring-webmvc-4.0.6.RELEASE.jar";
	}
	
	@Test
	public void test() {
		doJarTest();
	}
	
	/*protected boolean filter(String name) {
		return name.equals("org/springframework/util/concurrent/FutureAdapter.class");
	}*/

}
