package org.jasm.test.jar.maven;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JspApiReadWriteTest extends
		AbstractReadWriteMavenJarTestCase {

	
	@Test
	public void test() {
		doJarTest();
	}

	@Override
	protected MavenJarClassPathEntry getRootEntry() {
		return new MavenJarClassPathEntry("http://central.maven.org/maven2", "javax.servlet.jsp", "javax.servlet.jsp-api", "2.2.1");
	}

	@Override
	protected List<MavenJarClassPathEntry> getDependencies() {
		return new ArrayList<MavenJarClassPathEntry>();
	}

	

	

	
	protected boolean filter(String name) {
		return "javax/servlet/jsp/tagext/IterationTag.class".equals(name);
	}

}
