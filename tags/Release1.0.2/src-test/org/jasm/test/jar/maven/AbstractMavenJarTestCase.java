package org.jasm.test.jar.maven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;








import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMavenJarTestCase {
	
	protected abstract MavenJarClassPathEntry getRootEntry();
	protected abstract List<MavenJarClassPathEntry> getDependencies();
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	protected void debugClass(Clazz clazz) {
		if (log.isDebugEnabled()) {
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			PrettyPrinter printer = new PrettyPrinter(writer);
			printer.printItem(clazz);
			writer.close();
			log.debug("code:\n"+sw.toString());
		}
	}
	
	protected void doJarTest() {
		log.info("Testing "+getRootEntry().getName());
		File f = getRootEntry().getJarFile();
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
						testClass(IOUtils.toByteArray(data), entry.getName());
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
	
	protected abstract void testClass(byte[] data, String name);
	protected abstract boolean filter(String name);
	
	protected List<MavenJarClassPathEntry> parseClasspathEntries(String repositoryURL, String value) {
		List<MavenJarClassPathEntry> result = new ArrayList<MavenJarClassPathEntry>();
		LineNumberReader reader = new LineNumberReader(new StringReader(value));
		try {
			String line = reader.readLine();
			while (line != null) {
				if (line.trim().length()>0) {
					String [] parts = line.trim().split(":");
					if (parts.length==5) {
						if (parts[2].equals("jar")) {
							result.add(new MavenJarClassPathEntry(repositoryURL, parts[0], parts[1], parts[3]));
						}
					}
					
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
		
		
	}
	
	
	
}
