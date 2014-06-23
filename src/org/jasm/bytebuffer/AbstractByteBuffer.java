package org.jasm.bytebuffer;


public abstract class AbstractByteBuffer implements IByteBuffer {

    

	@Override
	public byte readByte(long offset) {
		return readByteArray(offset, 1)[0];
	}


	@Override
	public void writeByte(long offset, byte value) {
		writeByteArray(offset, new byte[]{value});
	}


	@Override
	public short readShort(long offset) {
		int s = readUnsignedShort(offset);
		return convertFromUnsigned(s);
	}


	@Override
	public void writeShort(long offset, short value) {
		int s = convertToUnsigned(value);
		writeUnsignedShort(offset, s);
	}


	@Override
	public int readInt(long offset) {
		long s = readUnsignedInt(offset);
		return convertFromUnsigned(s);
	}


	@Override
	public void writeInt(long offset, int value) {
		long s = convertToUnsigned(value);
		writeUnsignedInt(offset, s);
	}


	@Override
	public long readLong(long offset) {
		long l1 = readUnsignedInt(offset);
		long l2 = readUnsignedInt(offset+4);
		return l1<<32 | l2;
	}


	@Override
	public void writeLong(long offset, long value) {
		writeUnsignedInt(offset, value>>>32);
		writeUnsignedInt(offset+4, value&0xFFFFFFFFL);
	}


	@Override
	public short readUnsignedByte(long offset) {
		byte b = readByteArray(offset, 1)[0];
		return convertToUnsigned(b);
	}
	

	@Override
	public void writeUnsignedByte(long offset, short value) {
		byte[] b = new byte[1];
		b[0] = convertFromUnsigned(value);
		writeByteArray(offset, b);
	}

	@Override
	public int readUnsignedShort(long offset) {
		byte [] b = readByteArray(offset, 2);
		int s1 = (int)convertToUnsigned(b[0]);
		int s2 = (int)convertToUnsigned(b[1]);
		return s1<<8 | s2;
	}

	@Override
	public void writeUnsignedShort(long offset, int value) {
		byte [] b = new byte[2];
		b[0] = convertFromUnsigned((short) (value>>>8));
		b[1] = convertFromUnsigned((short)(value&0xFF));
		writeByteArray(offset, b);
		
	}

	@Override
	public long readUnsignedInt(long offset) {
		byte [] b = readByteArray(offset, 4);
		long s1 = (int)convertToUnsigned(b[0]);
		long s2 = (int)convertToUnsigned(b[1]);
		long s3 = (int)convertToUnsigned(b[2]);
		long s4 = (int)convertToUnsigned(b[3]);
		return s1<<24 | s2<<16  | s3<<8 | s4;
	}

	@Override
	public void writeUnsignedInt(long offset, long value) {
		byte [] b = new byte[4];
		b[0] = convertFromUnsigned((short) (value>>>24));
		b[1] = convertFromUnsigned((short) ((value>>>16)&0xFF));
		b[2] = convertFromUnsigned((short) ((value>>>8)&0xFF));
		b[3] = convertFromUnsigned((short)(value&0xFF));
		writeByteArray(offset, b);
	}
	
	
	
	@Override
	public float readFloat(long offset) {
		return Float.intBitsToFloat(readInt(offset));
	}


	@Override
	public void writeFloat(long offset, float value) {
		writeInt(offset, Float.floatToIntBits(value));
		
	}

	@Override
	public double readDouble(long offset) {
		return Double.longBitsToDouble(readLong(offset));
	}


	@Override
	public void writeDouble(long offset, double value) {
		writeLong(offset, Double.doubleToLongBits(value));
	}


	private short convertToUnsigned(byte b) {
		return (short)((b<0)?(0x100+b):b);
	}
	
	private byte convertFromUnsigned(short s) {
		return (byte)((s>0x7F)?(s-0x100):s);
	}
	
	private int convertToUnsigned(short b) {
		return (int)((b<0)?(0x10000+b):b);
	}
	
	private short convertFromUnsigned(int s) {
		return (short)((s>0x7FFF)?(s-0x10000):s);
	}
	
	private long convertToUnsigned(int b) {
		return (long)((b<0)?(0x100000000L+b):b);
	}
	
	private int convertFromUnsigned(long s) {
		return (int)((s>0x7FFFFFFF)?(s-0x100000000L):s);
	}

}
