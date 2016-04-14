package org.jasm.test.parser;

import org.jasm.item.attribute.SourceFileAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.junit.Assert;
import org.junit.Test;

public class MethodsFieldsParserTest extends AbstractParserTestCase {

	@Override
	protected String getDateiName() {
		return "MethodsFields.jasm";
	}
	
	@Test
	public void test() {
		Clazz clazz = doTest();
		
		Assert.assertNotNull(clazz);
		
		
		Assert.assertEquals(51, clazz.getMajorVersion());
		Assert.assertEquals(0, clazz.getMinorVersion());
		Assert.assertFalse(clazz.getModifier().isAbstract());
		Assert.assertFalse(clazz.getModifier().isAnnotation());
		Assert.assertFalse(clazz.getModifier().isEnum());
		Assert.assertFalse(clazz.getModifier().isFinal());
		Assert.assertFalse(clazz.getModifier().isInterface());
		Assert.assertTrue(clazz.getModifier().isPublic());
		Assert.assertTrue(clazz.getModifier().isSuper());
		Assert.assertFalse(clazz.getModifier().isSyntetic());
		
		Assert.assertEquals("org/jasm/test/testclass/Class1", clazz.getThisClass().getClassName());
		Assert.assertEquals("java/lang/Object", clazz.getSuperClass().getClassName());
		
		
		Assert.assertEquals("Class1.java", ((SourceFileAttributeContent)clazz.getAttributes().get(0).getContent()).getValueEntry().getValue());
		
		Assert.assertNotNull(clazz.getMethods().getMethod("<init>", "(Ljava/lang/String;)V"));
		Assert.assertNotNull(clazz.getFields().getField("STRING_CONSTANT", "Ljava/lang/String;"));
		Assert.assertNotNull(clazz.getFields().getField("SHORT_CONSTANT", "S"));
		
		Assert.assertEquals(1,clazz.getConstantPool().getFieldRefs("org/jasm/test/testclass/Class1", "STRING_CONSTANT", "Ljava/lang/String;").size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getNameAndTypeInfos("STRING_CONSTANT", "Ljava/lang/String;").size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getMethodRefs("java/lang/Object","<init>", "()V").size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getStringEntries("HELLO \n \\WORLD\r\t").size());
		
		Assert.assertEquals(2,clazz.getConstantPool().getIntegerEntries(1238).size());
		Assert.assertEquals(2,clazz.getConstantPool().getIntegerEntries(-1238).size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(0).size());
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(0x1.43333ap3f).size());
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(-0x1.43333ap3f).size());
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(Float.NaN).size());
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(Float.POSITIVE_INFINITY).size());
		Assert.assertEquals(1,clazz.getConstantPool().getFloatEntries(Float.NEGATIVE_INFINITY).size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getDoubleEntries(0).size());
		Assert.assertEquals(2,clazz.getConstantPool().getDoubleEntries(0x1.9066666666666p6).size());
		Assert.assertEquals(2,clazz.getConstantPool().getDoubleEntries(-0x1.9066666666666p6).size());
		Assert.assertEquals(1,clazz.getConstantPool().getDoubleEntries(Double.NaN).size());
		Assert.assertEquals(1,clazz.getConstantPool().getDoubleEntries(Double.POSITIVE_INFINITY).size());
		Assert.assertEquals(1,clazz.getConstantPool().getDoubleEntries(Double.NEGATIVE_INFINITY).size());
		
		Assert.assertEquals(1,clazz.getConstantPool().getLongEntries(10000).size());
		Assert.assertEquals(1,clazz.getConstantPool().getLongEntries(-10000).size());
	}

}
