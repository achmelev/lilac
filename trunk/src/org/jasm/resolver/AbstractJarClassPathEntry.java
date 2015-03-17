package org.jasm.resolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

import org.apache.commons.io.IOUtils;

public abstract class AbstractJarClassPathEntry extends AbstractBinaryClassPathEntry {
	
	private JarFile jar;
	
	@Override
	public byte[] findBytes(String resourceName) {
		try {
			
			createJarFile();
			JarEntry entry = jar.getJarEntry(resourceName);
			if (entry!=null && !entry.isDirectory()) {
				InputStream data = jar.getInputStream(entry);
				
				byte[] result = IOUtils.toByteArray(data);
				data.close();
				return result;
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public abstract File getJarFile(); 
	
	private synchronized void createJarFile() throws ZipException, IOException {
		if (jar == null) {
			jar = new JarFile(getJarFile());
		}
	}
	
	
}
