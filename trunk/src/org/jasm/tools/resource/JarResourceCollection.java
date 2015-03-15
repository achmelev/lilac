package org.jasm.tools.resource;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class JarResourceCollection implements ResourceCollection {
	
	private JarFile root;
	
	public JarResourceCollection(JarFile root) {
		this.root = root;
	}
	
	@Override
	public Enumeration<Resource> elements() {
		return new JarEntryEnumeration(root);
	}

}

class JarEntryEnumeration implements Enumeration<Resource> {
	
	private JarFile root;
	private Enumeration<JarEntry> entries;
	
	private JarEntry currentEntry = null;
	
	JarEntryEnumeration(JarFile jarFile) {
		this.root = jarFile;
		entries = jarFile.entries();
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
				if (currentEntry.isDirectory() || currentEntry.getName().equals("META-INF/MANIFEST.MF")) {
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
		JarEntry entry = currentEntry;
		currentEntry = null;
		return new JarEntryResource(root, entry);
	}
	
}
