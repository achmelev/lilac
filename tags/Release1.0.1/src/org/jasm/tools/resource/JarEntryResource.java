package org.jasm.tools.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarEntryResource implements Resource {
	
	private JarFile file;
	private JarEntry entry;
	
	public JarEntryResource(JarFile file, JarEntry entry) {
		this.file = file;
		this.entry = entry;
	}

	@Override
	public String getName() {
		return file.getName()+"!"+entry.getName();
	}

	@Override
	public InputStream createInputStream() {
		try {
			return file.getInputStream(entry);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
