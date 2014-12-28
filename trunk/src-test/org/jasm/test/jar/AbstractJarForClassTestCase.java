package org.jasm.test.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJarForClassTestCase {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	protected void doJarTest() {

		File f = getFile(getClazz());
		log.info("Testing "+f.getAbsolutePath());
		try {
			JarFile jar = new JarFile(f);
			Enumeration<JarEntry> entries = jar.entries();
			int errorCounter = 0;
			int counter = 0;
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(".class") && filter(entry.getName())) {
					InputStream data = jar.getInputStream(entry);
					try {
						testClass(IOUtils.toByteArray(data),f);
					} catch (Throwable e) {
						log.error("Error testing: "+entry.getName(),e);
						errorCounter++;
					}
					data.close();
					counter++;
				}
			}
			Assert.assertEquals(0, errorCounter);
			log.info("Successfully tested "+counter+" classes!");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private File getFile(Class clazz) {
		String resName = clazz.getName().replace('.', '/')+".class";
		URL url = Thread.currentThread().getContextClassLoader().getResource(resName);
		String urlS = url.toString();
		
		
		return new File(urlS.substring(urlS.indexOf('/')+1, urlS.indexOf('!')));
	}
	
	
	protected abstract void testClass(byte[] data, File jarFile);
	
	protected abstract Class getClazz();
	
	protected boolean filter(String name) {
		return true;
	}

}