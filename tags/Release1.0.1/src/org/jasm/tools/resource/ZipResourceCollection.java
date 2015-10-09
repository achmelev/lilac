package org.jasm.tools.resource;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceCollection implements ResourceCollection {
	
	private ZipFile root;
	
	public ZipResourceCollection(ZipFile root) {
		this.root = root;
	}
	
	@Override
	public Enumeration<Resource> elements() {
		return new ZipEntryEnumeration(root);
	}

}

class ZipEntryEnumeration implements Enumeration<Resource> {
	
	private ZipFile root;
	private Enumeration<? extends ZipEntry> entries;
	
	private ZipEntry currentEntry = null;
	
	ZipEntryEnumeration(ZipFile zipFile) {
		this.root = zipFile;
		entries = zipFile.entries();
	}

	@Override
	public boolean hasMoreElements() {

		if (currentEntry != null) {
			return true;
		} else if (!entries.hasMoreElements()) {
			return false;
		} else {
			while (currentEntry == null && entries.hasMoreElements()) {
				currentEntry = entries.nextElement();
				if (currentEntry.isDirectory()) {
					currentEntry = null;
				}
			}
			return currentEntry != null;
		}
		
	}

	@Override
	public Resource nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		ZipEntry entry = currentEntry;
		currentEntry = null;
		return new ZipEntryResource(root, entry);
	}
	
}
