package org.jasm.resolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ZipFileClassPathEntry extends AbstractBinaryClassPathEntry {
	
	private File zipFile;
	private ZipFile zip;
	
	public ZipFileClassPathEntry(File zipFile) {
		this.zipFile = zipFile;
	}
	
	@Override
	public byte[] findBytes(String resourceName) {
		try {
			createZipFile();
			ZipEntry entry = zip.getEntry(resourceName);
			if (entry!=null && !entry.isDirectory()) {
				InputStream data = zip.getInputStream(entry);
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
	
	private synchronized void createZipFile() throws ZipException, IOException {
		if (zip == null) {
			zip = new ZipFile(zipFile);
		}
	}

	@Override
	protected String getName() {
		return zipFile.getName();
	}	
	
}
