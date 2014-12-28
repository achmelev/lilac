package org.jasm.resolver;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBinaryClassPathEntry implements IClassPathEntry {
	
	private boolean invalid = false;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ExternalClassInfo findClass(String className) {
		
		try {
			byte [] data = findBytes(className);
			if (data == null) {
				return null;
			}
			ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
			Clazz clazz = new Clazz();
			clazz.read(bbuf, 0L);
			clazz.resolve();
			
			return ExternalClassInfo.createFromClass(clazz);
		} catch (Throwable e) {
			log.warn("Error reading from "+getName()+", marked invalid, error message: ",e );
			invalid = true;
			return null;
		}
	}
	
	protected abstract byte[] findBytes(String className);
	protected abstract String getName();

	public boolean isInvalid() {
		return invalid;
	}
	
	
}
