package org.jasm.item.classpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import org.apache.commons.io.IOUtils;

public class DirClasspathEntry extends AbstractBinaryClassPathEntry {
	
	private File rootDir;
	private boolean invalid = false;
	
	public DirClasspathEntry(File rootDir) {
		if (!(rootDir.exists() && rootDir.isDirectory())) {
			invalid = true;
		} else {
			this.rootDir = rootDir;
		}
	}

	@Override
	protected byte[] findBytes(String className) {
		if (!invalid) {
			File clazzFile = new File(rootDir, className+".class");
			if (clazzFile.exists() && clazzFile.isFile()) {
				byte [] result = new byte[className.length()];
				try {
					FileInputStream stream = new FileInputStream(clazzFile);
					IOUtils.readFully(stream, result);
					return result;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
