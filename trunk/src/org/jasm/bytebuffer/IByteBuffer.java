package org.jasm.bytebuffer;

/**
 * 
 * @author ac
 * All ByteBuffers implement this interface
 */
public interface IByteBuffer {
	
	public byte[] readByteArray(long offset, int length);
	public void   writeByteArray(long offset, byte[] value);
	public short  readUnsignedByte(long offset);
	public void   writeUnsignedByte(long offset, short value);
	public int    readUnsignedShort(long offset);
	public void   writeUnsignedShort(long offset, int value);
	public long   readUnsignedInt(long offset);
	public void   writeUnsignedInt(long offset, long value);
	
	public byte   readByte(long offset);
	public void   writeByte(long offset, byte value);
	public short  readShort(long offset);
	public void   writeShort(long offset, short value);
	public int    readInt(long offset);
	public void   writeInt(long offset, int value);
	public long   readLong(long offset);
	public void   writeLong(long offset, long value);
	
	public float   readFloat(long offset);
	public void    writeFloat(long offset, float value);
	
	public double   readDouble(long offset);
	public void     writeDouble(long offset,  double value);

}
