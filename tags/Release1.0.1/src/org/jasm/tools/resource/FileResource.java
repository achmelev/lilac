package org.jasm.tools.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResource implements Resource {
	
	private File f;
	
	public FileResource(File f) {
		if (!f.exists() || f.isDirectory()) {
			throw new IllegalArgumentException(f.getAbsolutePath()+" doesn't exist or is a directory!");
		}
		this.f = f;
	}

	@Override
	public String getName() {
		return f.getAbsolutePath();
	}

	@Override
	public InputStream createInputStream() {
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
