package org.jasm.item.classpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import org.apache.commons.io.IOUtils;

public abstract class AbstractJarClassPathEntry extends AbstractBinaryClassPathEntry {
	
	private JarFile jar;
	
	@Override
	protected byte[] findBytes(String className) {
		try {
			if (jar == null) {
				jar = new JarFile(getJarFile());
			}
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				
				if (!entry.isDirectory() && entry.getName().equals(className+".class")) {
					InputStream data = jar.getInputStream(entry);
					
					byte[] result = IOUtils.toByteArray(data);
					data.close();
					return result;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	public abstract File getJarFile(); 
	
	
}