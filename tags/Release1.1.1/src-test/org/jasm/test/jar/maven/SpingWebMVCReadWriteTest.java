package org.jasm.test.jar.maven;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SpingWebMVCReadWriteTest extends
		AbstractReadWriteMavenJarTestCase {

	
	@Test
	public void test() {
		doJarTest();
	}

	@Override
	protected MavenJarClassPathEntry getRootEntry() {
		return new MavenJarClassPathEntry("http://central.maven.org/maven2", "org.springframework", "spring-webmvc", "4.0.6.RELEASE");
	}

	@Override
	protected List<MavenJarClassPathEntry> getDependencies() {
		return parseClasspathEntries("http://central.maven.org/maven2", getDepsList());
	}

	
	/*protected boolean filter(String name) {
		return "org/springframework/asm/Frame.class".equals(name);
	}*/
	
	private String getDepsList() {
		return "org.springframework:spring-core:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.apache.tiles:tiles-velocity:jar:3.0.4:compile\r\n" + 
				"org.springframework:spring-web:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.apache.tiles:tiles-jsp:jar:2.2.2:compile\r\n" + 
				"log4j:log4j:jar:1.2.14:compile\r\n" + 
				"oro:oro:jar:2.0.8:compile\r\n" + 
				"commons-digester:commons-digester:jar:2.1:compile\r\n" + 
				"commons-beanutils:commons-beanutils:jar:1.8.0:compile\r\n" + 
				"org.apache.tiles:tiles-servlet:jar:3.0.4:compile\r\n" + 
				"com.github.spullara.mustache.java:compiler:jar:0.8.4:compile\r\n" + 
				"org.apache.tiles:tiles-request-api:jar:1.0.4:compile\r\n" + 
				"commons-logging:commons-logging:jar:1.1.1:compile\r\n" + 
				"org.apache.tiles:tiles-request-freemarker:jar:1.0.4:compile\r\n" + 
				"org.codehaus.jackson:jackson-mapper-asl:jar:1.9.13:compile\r\n" + 
				"eclipse:jdtcore:jar:3.1.0:compile\r\n" + 
				"commons-collections:commons-collections:jar:2.1:compile\r\n" + 
				"org.springframework:spring-expression:jar:4.0.6.RELEASE:compile\r\n" + 
				"bouncycastle:bcmail-jdk14:jar:138:compile\r\n" + 
				"org.freemarker:freemarker:jar:2.3.20:compile\r\n" + 
				"org.bouncycastle:bctsp-jdk14:jar:1.38:compile\r\n" + 
				"org.slf4j:slf4j-api:jar:1.7.6:compile\r\n" + 
				"javax.servlet:javax.servlet-api:jar:3.0.1:provided\r\n" + 
				"org.apache.tiles:tiles-template:jar:2.2.2:compile\r\n" + 
				"org.apache.tiles:tiles-el:jar:2.2.2:compile\r\n" + 
				"jboss:javassist:jar:3.7.ga:compile\r\n" + 
				"org.springframework:spring-context:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.apache.tiles:tiles-mvel:jar:3.0.4:compile\r\n" + 
				"velocity-tools:velocity-tools-view:jar:1.4:compile\r\n" + 
				"com.fasterxml.jackson.core:jackson-core:jar:2.3.3:compile\r\n" + 
				"org.apache.tiles:tiles-core:jar:3.0.4:compile\r\n" + 
				"net.sourceforge.jexcelapi:jxl:jar:2.6.12:compile\r\n" + 
				"javax.servlet.jsp:javax.servlet.jsp-api:jar:2.2.1:compile\r\n" + 
				"aopalliance:aopalliance:jar:1.0:compile\r\n" + 
				"javax.el:javax.el-api:jar:2.2.5:compile\r\n" + 
				"org.apache.tiles:tiles-request-mustache:jar:1.0.4:compile\r\n" + 
				"org.springframework:spring-oxm:jar:4.0.6.RELEASE:compile\r\n" + 
				"com.fasterxml.jackson.core:jackson-annotations:jar:2.3.0:compile\r\n" + 
				"com.google.guava:guava:jar:12.0.1:compile\r\n" + 
				"javax.servlet.jsp.jstl:javax.servlet.jsp.jstl-api:jar:1.2.1:compile\r\n" + 
				"commons-codec:commons-codec:jar:1.5:compile\r\n" + 
				"org.apache.tiles:tiles-request-servlet:jar:1.0.4:compile\r\n" + 
				"com.google.code.findbugs:jsr305:jar:1.3.9:compile\r\n" + 
				"org.codehaus.castor:castor:jar:1.2:compile\r\n" + 
				"org.apache.tiles:tiles-freemarker:jar:3.0.4:compile\r\n" + 
				"com.lowagie:itext:jar:2.1.7:compile\r\n" + 
				"bouncycastle:bcprov-jdk14:jar:138:compile\r\n" + 
				"jfree:jcommon:jar:1.0.15:compile\r\n" + 
				"org.apache.tiles:tiles-request-velocity:jar:1.0.4:compile\r\n" + 
				"org.apache.tiles:tiles-compat:jar:3.0.4:compile\r\n" + 
				"commons-lang:commons-lang:jar:2.4:compile\r\n" + 
				"org.apache.poi:poi:jar:3.10-FINAL:compile\r\n" + 
				"net.sf.jasperreports:jasperreports:jar:5.5.1:compile\r\n" + 
				"org.apache.tiles:tiles-api:jar:2.2.2:compile\r\n" + 
				"org.springframework:spring-aop:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.springframework:spring-beans:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.springframework:spring-context-support:jar:4.0.6.RELEASE:compile\r\n" + 
				"org.bouncycastle:bcmail-jdk14:jar:1.38:compile\r\n" + 
				"rome:rome:jar:1.0:compile\r\n" + 
				"org.codehaus.jackson:jackson-core-asl:jar:1.9.13:compile\r\n" + 
				"org.apache.tiles:tiles-ognl:jar:3.0.4:compile\r\n" + 
				"org.bouncycastle:bcprov-jdk14:jar:1.38:compile\r\n" + 
				"ognl:ognl:jar:2.7.3:compile\r\n" + 
				"org.apache.tiles:tiles-extras:jar:3.0.4:compile\r\n" + 
				"jdom:jdom:jar:1.0:compile\r\n" + 
				"com.fasterxml.jackson.core:jackson-databind:jar:2.3.3:compile\r\n" + 
				"jfree:jfreechart:jar:1.0.12:compile\r\n" + 
				"org.apache.velocity:velocity-tools:jar:2.0:compile\r\n" + 
				"org.mvel:mvel2:jar:2.0.11:compile\r\n" + 
				"org.apache.tiles:tiles-request-servlet-wildcard:jar:1.0.4:compile\r\n" + 
				"org.apache.velocity:velocity:jar:1.7:compile";
	}

}
