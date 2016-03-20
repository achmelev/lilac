package org.jasm.test.constantpool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Assert;

import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodHandleInfo.MethodHandleReferenceKind;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.constantpool.NameAndTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.test.item.DummyRoot;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class ConstantPoolTest {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() {
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream stream = cl.getResourceAsStream("org/jasm/test/testclass/Class1.class");
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
		
		ConstantPool pool = new ConstantPool();
		ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
		pool.read(bbuf, 8);
		
		pool.setParent(new Clazz());
		pool.resolve();
		pool.updateMetadata();
		
		StringWriter sw = new StringWriter();
		PrintWriter writer = new PrintWriter(sw);
		PrettyPrinter printer = new PrettyPrinter(writer);
		printer.printItem(pool);
		writer.close();
		log.debug("code: \n"+sw.toString());
		
		byte [] data1 = new byte[pool.getLength()];
		byte [] data2 = new byte[pool.getLength()];
		ByteArrayByteBuffer bbuf2 = new ByteArrayByteBuffer(data2);
		System.arraycopy(data, 8, data1, 0, data1.length);
		pool.write(bbuf2, 0);
		
		assertArrayEquals(data1, data2);
		
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getUtf8Infos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getUtf8Infos("org/jasm/test/testclass/Class1").contains(pool.getClassInfos("org/jasm/test/testclass/Class1").get(0).getClassNameReference()));
		
		assertTrue(pool.getStringEntries("HELLO WORLD").size()>0);
		assertTrue(pool.getUtf8Infos("HELLO WORLD").size()>0);
		assertTrue(pool.getUtf8Infos("HELLO WORLD").contains(pool.getStringEntries("HELLO WORLD").get(0).getUtf8Reference()));
		
		assertTrue(pool.getMethodRefs("java/lang/Object", "<init>", "()V").size()>0);
		assertTrue(pool.getClassInfos("java/lang/Object").size()>0);
		assertTrue(pool.getNameAndTypeInfos("<init>", "()V").size()>0);
		assertTrue(pool.getNameAndTypeInfos("<init>", "()V").contains(pool.getMethodRefs("java/lang/Object", "<init>", "()V").get(0).getNameAndTypeReference()));
		assertTrue(pool.getClassInfos("java/lang/Object").contains(pool.getMethodRefs("java/lang/Object", "<init>", "()V").get(0).getClassReference()));
		
		assertTrue(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").size()>0);
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").size()>0);
		assertTrue(pool.getNameAndTypeInfos("STRING_CONSTANT", "Ljava/lang/String;").size()>0);
		assertTrue(pool.getNameAndTypeInfos("STRING_CONSTANT", "Ljava/lang/String;").contains(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").get(0).getNameAndTypeReference()));
		assertTrue(pool.getClassInfos("org/jasm/test/testclass/Class1").contains(pool.getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").get(0).getClassReference()));
		
		assertTrue(pool.getLongEntries(10000).size()>0);
		assertTrue(pool.getFloatEntries(10.1f).size()>0);
		assertTrue(pool.getDoubleEntries(100.1).size()>0);
	}
	
	@Test
	public void testGetOrAdd() {
		ConstantPool cp = new ConstantPool();
		Utf8Info utf8 = cp.getOrAddUtf8nfo("Hello world");
		Assert.assertEquals("Hello world", utf8.getValue());
		Assert.assertEquals(1, cp.getSize());
		Assert.assertSame(utf8, cp.getOrAddUtf8nfo("Hello world"));
		StringInfo si = cp.getOrAddStringInfo("Hello world");
		Assert.assertEquals("Hello world", si.getContent());
		Assert.assertEquals(2, cp.getSize());
		Assert.assertSame(si, cp.getOrAddStringInfo("Hello world"));
		si = cp.getOrAddStringInfo("Hello world2");
		Assert.assertEquals("Hello world2", si.getContent());
		Assert.assertEquals(4, cp.getSize());
		Assert.assertSame(si, cp.getOrAddStringInfo("Hello world2"));
		cp.getOrAddUtf8nfo("java/lang/String");
		Assert.assertEquals(5, cp.getSize());
		ClassInfo ci = cp.getOrAddClassInfo("java/lang/String");
		Assert.assertEquals("java/lang/String", ci.getClassName());
		Assert.assertEquals(6, cp.getSize());
		Assert.assertSame(ci, cp.getOrAddClassInfo("java/lang/String"));
		ci = cp.getOrAddClassInfo("java/lang/Runnable");
		Assert.assertEquals("java/lang/Runnable", ci.getClassName());
		Assert.assertEquals(8, cp.getSize());
		Assert.assertSame(ci, cp.getOrAddClassInfo("java/lang/Runnable"));
		cp.getOrAddClassInfo("java/lang/System");
		Assert.assertEquals(10, cp.getSize());
		cp.getOrAddUtf8nfo("out");
		Assert.assertEquals(11, cp.getSize());
		cp.getOrAddUtf8nfo("Ljava/io/PrintStream;");
		Assert.assertEquals(12, cp.getSize());
		NameAndTypeInfo ni = cp.getOrAddNameAndTypeInfo("out", "Ljava/io/PrintStream;");
		Assert.assertEquals("out", ni.getName());
		Assert.assertEquals("Ljava/io/PrintStream;", ni.getDescriptor());
		Assert.assertEquals(13, cp.getSize());
		Assert.assertSame(ni, cp.getOrAddNameAndTypeInfo("out", "Ljava/io/PrintStream;"));
		ni = cp.getOrAddNameAndTypeInfo("in", "Ljava/io/InputStream;");
		Assert.assertEquals("in", ni.getName());
		Assert.assertEquals("Ljava/io/InputStream;", ni.getDescriptor());
		Assert.assertEquals(16, cp.getSize());
		Assert.assertSame(ni, cp.getOrAddNameAndTypeInfo("in", "Ljava/io/InputStream;"));
		FieldrefInfo fi = cp.getOrAddFieldrefInfo("java/lang/System", "out", "Ljava/io/PrintStream;");
		Assert.assertEquals(17, cp.getSize());
		Assert.assertEquals("out", fi.getName());
		Assert.assertEquals("Ljava/io/PrintStream;", fi.getDescriptor());
		Assert.assertEquals("java/lang/System", fi.getClassName());
		Assert.assertSame(fi, cp.getOrAddFieldrefInfo("java/lang/System", "out", "Ljava/io/PrintStream;"));
		fi = cp.getOrAddFieldrefInfo("java/lang/Double", "value", "I");
		Assert.assertEquals(23, cp.getSize());
		Assert.assertEquals("value", fi.getName());
		Assert.assertEquals("I", fi.getDescriptor());
		Assert.assertEquals("java/lang/Double", fi.getClassName());
		Assert.assertSame(fi, cp.getOrAddFieldrefInfo("java/lang/Double", "value", "I"));
		cp.getOrAddNameAndTypeInfo("toString", "()Ljava/lang/String;");
		Assert.assertEquals(26, cp.getSize());
		MethodrefInfo mi = cp.getOrAddMethofdrefInfo("java/lang/Double", "toString", "()Ljava/lang/String;");
		Assert.assertEquals(27, cp.getSize());
		Assert.assertEquals("toString", mi.getName());
		Assert.assertEquals("()Ljava/lang/String;", mi.getDescriptor());
		Assert.assertEquals("java/lang/Double", mi.getClassName());
		Assert.assertSame(mi, cp.getOrAddMethofdrefInfo("java/lang/Double", "toString", "()Ljava/lang/String;"));
		mi = cp.getOrAddMethofdrefInfo("java/lang/Integer", "intValue", "()I");
		Assert.assertEquals(33, cp.getSize());
		Assert.assertEquals("intValue", mi.getName());
		Assert.assertEquals("()I", mi.getDescriptor());
		Assert.assertEquals("java/lang/Integer", mi.getClassName());
		Assert.assertSame(mi, cp.getOrAddMethofdrefInfo("java/lang/Integer", "intValue", "()I"));
		cp.getOrAddNameAndTypeInfo("run", "()V");
		Assert.assertEquals(36, cp.getSize());
		InterfaceMethodrefInfo imi = cp.getOrAddInterfaceMethofdrefInfo("java/lang/Runnable", "run", "()V");
		Assert.assertEquals(37, cp.getSize());
		Assert.assertEquals("run", imi.getName());
		Assert.assertEquals("()V", imi.getDescriptor());
		Assert.assertEquals("java/lang/Runnable", imi.getClassName());
		Assert.assertSame(imi, cp.getOrAddInterfaceMethofdrefInfo("java/lang/Runnable", "run", "()V"));
		imi = cp.getOrAddInterfaceMethofdrefInfo("java/lang/Clonable", "clone", "()V");
		Assert.assertEquals(42, cp.getSize());
		Assert.assertEquals("clone", imi.getName());
		Assert.assertEquals("()V", imi.getDescriptor());
		Assert.assertEquals("java/lang/Clonable", imi.getClassName());
		Assert.assertSame(imi, cp.getOrAddInterfaceMethofdrefInfo("java/lang/Clonable", "clone", "()V"));
		Assert.assertEquals(42, cp.getSize());
		DoubleInfo di = cp.getOrAddDoubleInfo(3.5);
		Assert.assertEquals(new Double(3.5), di.getValue());
		Assert.assertSame(di, cp.getOrAddDoubleInfo(3.5));
		Assert.assertEquals(44, cp.getSize());
		LongInfo li = cp.getOrAddLongInfo(1000L);
		Assert.assertEquals(new Long(1000L), li.getValue());
		Assert.assertSame(li, cp.getOrAddLongInfo(1000L));
		Assert.assertEquals(46, cp.getSize());
		IntegerInfo ii = cp.getOrAddIntegerInfo(100);
		Assert.assertEquals(new Integer(100), ii.getValue());
		Assert.assertSame(ii, cp.getOrAddIntegerInfo(100));
		Assert.assertEquals(47, cp.getSize());
		FloatInfo fli = cp.getOrAddFloatInfo(2.5f);
		Assert.assertEquals(new Float(2.5f), fli.getValue());
		Assert.assertSame(fli, cp.getOrAddFloatInfo(2.5f));
		Assert.assertEquals(48, cp.getSize());
		MethodTypeInfo mti = cp.getOrAddMethodTypeInfo("()V");
		Assert.assertEquals("()V", mti.getDescriptor());
		Assert.assertSame(mti, cp.getOrAddMethodTypeInfo("()V"));
		Assert.assertEquals(49, cp.getSize());
		mti = cp.getOrAddMethodTypeInfo("()Z");
		Assert.assertEquals("()Z", mti.getDescriptor());
		Assert.assertSame(mti, cp.getOrAddMethodTypeInfo("()Z"));
		Assert.assertEquals(51, cp.getSize());
		MethodHandleInfo mhi = cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.GET_FIELD, "java/lang/System", "out", "Ljava/io/PrintStream;", false);
		Assert.assertEquals("java/lang/System", mhi.getReference().getClassName());
		Assert.assertEquals("out", mhi.getReference().getName());
		Assert.assertEquals("Ljava/io/PrintStream;", mhi.getReference().getDescriptor());
		Assert.assertSame(mhi, cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.GET_FIELD, "java/lang/System", "out", "Ljava/io/PrintStream;", false));
		Assert.assertEquals(52, cp.getSize());
		mhi = cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.INVOKE_STATIC, "org/jasm/Test", "testMethod", "()V", false);
		Assert.assertEquals("org/jasm/Test", mhi.getReference().getClassName());
		Assert.assertEquals("testMethod", mhi.getReference().getName());
		Assert.assertEquals("()V", mhi.getReference().getDescriptor());
		Assert.assertTrue(mhi.getReference() instanceof MethodrefInfo);
		Assert.assertEquals(58, cp.getSize());
		mhi = cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.INVOKE_STATIC, "org/jasm/Test2", "testMethod2", "()V", true);
		Assert.assertEquals("org/jasm/Test2", mhi.getReference().getClassName());
		Assert.assertEquals("testMethod2", mhi.getReference().getName());
		Assert.assertEquals("()V", mhi.getReference().getDescriptor());
		Assert.assertTrue(mhi.getReference() instanceof InterfaceMethodrefInfo);
		Assert.assertEquals(64, cp.getSize());
		mhi = cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.INVOKE_VIRTUAL, "org/jasm/Test3", "testMethod3", "()V", true);
		Assert.assertEquals("org/jasm/Test3", mhi.getReference().getClassName());
		Assert.assertEquals("testMethod3", mhi.getReference().getName());
		Assert.assertEquals("()V", mhi.getReference().getDescriptor());
		Assert.assertTrue(mhi.getReference() instanceof MethodrefInfo);
		Assert.assertEquals(70, cp.getSize());
		mhi = cp.getOrAddMethodHandleInfo(MethodHandleReferenceKind.INVOKE_INTERFACE, "org/jasm/Test4", "testMethod4", "()V", true);
		Assert.assertEquals("org/jasm/Test4", mhi.getReference().getClassName());
		Assert.assertEquals("testMethod4", mhi.getReference().getName());
		Assert.assertEquals("()V", mhi.getReference().getDescriptor());
		Assert.assertTrue(mhi.getReference() instanceof InterfaceMethodrefInfo);
		Assert.assertEquals(76, cp.getSize());
		
		
	}
	
	

}
