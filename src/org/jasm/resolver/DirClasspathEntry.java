package org.jasm.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class DirClasspathEntry extends AbstractBinaryClassPathEntry {
	
	private File rootDir;
	
	public DirClasspathEntry(File rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public byte[] findBytes(String resourceName) {
		
		if (!(rootDir.exists() && rootDir.isDirectory())) {
			throw new IllegalArgumentException(rootDir.getAbsolutePath()+" doesn't exist or isn't a directory!");
		}

		File clazzFile = new File(rootDir, resourceName);
		if (clazzFile.exists() && clazzFile.isFile()) {
			byte [] result = new byte[(int)clazzFile.length()];
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
		
	}

	@Override
	protected String getName() {
		return rootDir.getAbsolutePath();
	}

}
