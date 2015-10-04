package org.jasm.test.bytebuffer;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ByteArrayBufferTest {
	
	@Test
	public void test() {
		byte[] data = new byte[200];
		ByteArrayByteBuffer b = new ByteArrayByteBuffer(data);
		
		//Byte
		byte s = 5;
		b.writeByte(5, s);
		assertEquals(s, b.readByte(5));
		
		s = -5;
		b.writeByte(5, s);
		assertEquals(s, b.readByte(5));
		
		s = 127;
		b.writeByte(5, s);
		assertEquals(s, b.readByte(5));
		
		s = -128;
		b.writeByte(5, s);
		assertEquals(s, b.readByte(5));
		
		//Unsigned Byte
		short s1 = 5;
		b.writeUnsignedByte(5, s1);
		assertEquals(s1, b.readUnsignedByte(5));
		
		s1 = 0;
		b.writeUnsignedByte(5, s1);
		assertEquals(s1, b.readUnsignedByte(5));
		
		s1 = 0xFF;
		b.writeUnsignedByte(5, s1);
		assertEquals(s1, b.readUnsignedByte(5));
		
		//Short
		short s2 = 5;
		b.writeShort(5, s2);
		assertEquals(s2, b.readShort(5));
		
		s2 = -5;
		b.writeShort(5, s2);
		assertEquals(s2, b.readShort(5));
		
		s2 = 32767;
		b.writeShort(5, s2);
		assertEquals(s2, b.readShort(5));
		
		s2 = -32768;
		b.writeShort(5, s2);
		assertEquals(s2, b.readShort(5));
		
		//Unsigned Short
		int s3 = 5;
		b.writeUnsignedShort(5, s3);
		assertEquals(s3, b.readUnsignedShort(5));
		
		s3 = 0;
		b.writeUnsignedShort(5, s3);
		assertEquals(s3, b.readUnsignedShort(5));
		
		s3 = 0xFFFF;
		b.writeUnsignedShort(5, s3);
		assertEquals(s3, b.readUnsignedShort(5));
		
		//Int
		int s4= 5;
		b.writeInt(5, s4);
		assertEquals(s4, b.readInt(5));
		
		s4= -5;
		b.writeInt(5, s4);
		assertEquals(s4, b.readInt(5));
		
		s4= 2^31-1;
		b.writeInt(5, s4);
		assertEquals(s4, b.readInt(5));
		
		s4= -2^31;
		b.writeInt(5, s4);
		assertEquals(s4, b.readInt(5));
		
		//Unsigned Int
		long s5 = 5;
		b.writeUnsignedInt(5, s5);
		assertEquals(s5, b.readUnsignedInt(5));
		
		s5 = 0;
		b.writeUnsignedInt(5, s5);
		assertEquals(s5, b.readUnsignedInt(5));
		
		s5 = 0xFFFFFFFFL;
		b.writeUnsignedInt(5, s5);
		assertEquals(s5, b.readUnsignedInt(5));
		
		//long
		long s6= 5;
		b.writeLong(5, s6);
		assertEquals(s6, b.readLong(5));
		
		s6= -5;
		b.writeLong(5, s6);
		assertEquals(s6, b.readLong(5));
		
		s6= 2^63-1;
		b.writeLong(5, s6);
		assertEquals(s6, b.readLong(5));
		
		s6= -2^63;
		b.writeLong(5, s6);
		assertEquals(s6, b.readLong(5));
		
		//float
		float f = 5.5f;
		b.writeFloat(5, f);
		assertEquals(f, b.readFloat(5),0.0);
		
		f = -5.5f;
		b.writeFloat(5, f);
		assertEquals(f, b.readFloat(5),0.0);
		
		//float
		double d = 5.5;
		b.writeDouble(5, d);
		assertEquals(d, b.readDouble(5),0.0);
		
		d = -5.5;
		b.writeDouble(5, d);
		assertEquals(d, b.readDouble(5),0.0);
		
		//String 
		String st = "Hello World mit Ümläten";
		b.writeUTF8(5, st);
		assertEquals(st, b.readUTF8(5).getValue());
		assertEquals(st.length()+2+2, b.readUTF8(5).getLength());
		
		
	}
	
}
