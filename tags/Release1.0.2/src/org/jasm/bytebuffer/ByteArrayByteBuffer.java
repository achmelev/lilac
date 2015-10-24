package org.jasm.bytebuffer;

public class ByteArrayByteBuffer extends AbstractByteBuffer {
	
	private byte [] data = null;  
	
	public ByteArrayByteBuffer(byte[] data) {
		this.data = data;
	}

	@Override
	public byte[] readByteArray(long offset, int length) {
		if (offset+length>data.length) {
			throw new IndexOutOfBoundsException(offset+"+"+length+">"+data.length);
		}
		byte [] result = new byte[length];
		System.arraycopy(data, (int)offset, result, 0, length);
		return result;
	}

	@Override
	public void writeByteArray(long offset, byte[] value) {
		if (offset+value.length>data.length) {
			throw new IndexOutOfBoundsException(offset+"+"+value.length+">"+data.length);
		}
		System.arraycopy(value, 0, data, (int)offset, value.length);
	}

}
