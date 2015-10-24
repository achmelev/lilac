package org.jasm.test.jar.maven;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SpingCoreGenerateStackMapsTest extends
		AbstractGenerateStackMapsMavenJarTestCase {

	
	@Test
	public void test() {
		doJarTest();
	}

	@Override
	protected MavenJarClassPathEntry getRootEntry() {
		return new MavenJarClassPathEntry("http://central.maven.org/maven2", "org.springframework", "spring-core", "4.0.6.RELEASE");
	}

	@Override
	protected List<MavenJarClassPathEntry> getDependencies() {
		List<MavenJarClassPathEntry> result = new ArrayList<MavenJarClassPathEntry>();
		result.add(new MavenJarClassPathEntry("http://central.maven.org/maven2", "log4j", "log4j", "1.2.17"));
		result.add(new MavenJarClassPathEntry("http://central.maven.org/maven2", "org.aspectj", "aspectjweaver", "1.8.1"));
		result.add(new MavenJarClassPathEntry("http://central.maven.org/maven2", "commons-logging", "commons-logging", "1.1.3"));
		result.add(new MavenJarClassPathEntry("http://central.maven.org/maven2", "net.sf.jopt-simple", "jopt-simple", "4.6"));
		
		return result;
	}

	
	protected boolean filter(String name) {
		return !"org/springframework/cglib/transform/AbstractTransformTask.class".equals(name)
		&& !"org/springframework/cglib/transform/AbstractProcessTask.class".equals(name)		
		/**&& "org/springframework/util/ConcurrentReferenceHashMap$TaskOption.class".equals(name)**/
		;
	}

}
