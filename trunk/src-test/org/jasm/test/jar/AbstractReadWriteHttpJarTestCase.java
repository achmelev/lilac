package org.jasm.test.jar;

import static org.junit.Assert.*;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.item.clazz.Clazz;

public abstract class AbstractReadWriteHttpJarTestCase extends
		AbstractHttpJarTestCase {
	
	@Override
	protected void testClass(byte[] data) {
		
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		Clazz clazz = new Clazz();
		clazz.read(bbuf, 0L);
		clazz.resolve();
		clazz.updateMetadata();
		debugClass(clazz);
		byte [] data2 = new byte[clazz.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		clazz.write(bbuf2, 0);
		assertArrayEquals(data2, data);
	}
	

}
