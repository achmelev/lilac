package org.jasm.item.classpath;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderClasspathEntry extends AbstractBinaryClassPathEntry {
	
	private ClassLoader loader;
	
	public ClassLoaderClasspathEntry(ClassLoader loader) {
		this.loader = loader;
	}

	@Override
	protected byte[] findBytes(String className) {
		InputStream stream = loader.getResourceAsStream(className+".class");
		if (stream != null) {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			byte [] buf = new byte[1024];
			try {
				int read = stream.read(buf);
				while (read>=0) {
					if (read>0) {
						bo.write(buf, 0, read);
					}
					read = stream.read(buf);
				}
				bo.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			byte [] data = bo.toByteArray();
			return data;
		} else {
			return null;
		}
	}

	@Override
	protected String getName() {
		return "loader: "+loader.getClass().getName();
	}

}
