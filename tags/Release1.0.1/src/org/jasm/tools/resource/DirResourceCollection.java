package org.jasm.tools.resource;

import java.io.File;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class DirResourceCollection implements ResourceCollection {

	private File root;
	
	public DirResourceCollection(File root) {
		this.root = root;
	}

	@Override
	public Enumeration<Resource> elements() {
		return new DirResourceEnumeration(root);
	}
	
	

}

class DirResourceEnumeration implements Enumeration<Resource> {
	
	private File[] children;
	private DirResourceEnumeration currentDir;
	int index = -1;
	
	DirResourceEnumeration(File root) {
		if (!root.exists() || !root.isDirectory()) {
			throw new IllegalArgumentException(root.getAbsolutePath()+" doesn't exist or isn't a directory");
		}
		children = root.listFiles();
		index = 0;
	}

	@Override
	public boolean hasMoreElements() {
		if (index>=children.length) {
			return false;
		} else if (children[index].isFile()) {
			return true;
		} else if (currentDir != null && currentDir.hasMoreElements()) {
			return true;
		} else if (currentDir == null || !currentDir.hasMoreElements()) {
			if (currentDir == null) {
				currentDir = new DirResourceEnumeration(children[index]);
			}
			if (currentDir.hasMoreElements()) {
				return true;
			}
			while (!currentDir.hasMoreElements() && index<children.length && !children[index].isFile()) {
				index++;
				if (index<children.length && children[index].isDirectory()) {
					currentDir = new DirResourceEnumeration(children[index]);
				}
			}
			if (currentDir.hasMoreElements()) {
				return true;
			}else if (index>=children.length) {
				return false;
			} else if (children[index].isFile()) {
				return true;
			} else {
				throw new IllegalStateException();
			}
			
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public Resource nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		Resource result = null;
		if (children[index].isFile()) {
			result =  new FileResource(children[index]);
			index++;
			currentDir = null;
			return result;
		} else {
			result =  currentDir.nextElement();
			if (!currentDir.hasMoreElements()) {
				index++;
				currentDir = null;
			}
			return result;
		}
	}
	
	
}
