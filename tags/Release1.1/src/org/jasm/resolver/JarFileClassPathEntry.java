package org.jasm.resolver;

import java.io.File;

public class JarFileClassPathEntry extends AbstractJarClassPathEntry {
	
	private File jarFile;
	
	public JarFileClassPathEntry(File jarFile) {
		this.jarFile = jarFile;
		
	}

	@Override
	public File getJarFile() {
		return jarFile;
	}

	@Override
	protected String getName() {
		return jarFile.getName();
	}

}
