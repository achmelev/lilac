package org.jasm.tools.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipEntryResource implements Resource {
	
	private ZipFile file;
	private ZipEntry entry;
	
	public ZipEntryResource(ZipFile file, ZipEntry entry) {
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
