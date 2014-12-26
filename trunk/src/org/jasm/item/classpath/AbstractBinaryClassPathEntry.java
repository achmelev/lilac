package org.jasm.item.classpath;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;

public abstract class AbstractBinaryClassPathEntry implements IClassPathEntry {
	
	public ExternalClassInfo findClass(String className) {
		
		byte [] data = findBytes(className);
		if (data == null) {
			return null;
		}
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		Clazz clazz = new Clazz();
		clazz.read(bbuf, 0L);
		clazz.resolve();
		
		return ExternalClassInfo.createFromClass(clazz);
	}
	
	protected abstract byte[] findBytes(String className);
}
